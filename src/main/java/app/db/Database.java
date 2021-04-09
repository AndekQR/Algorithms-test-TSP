package app.db;

import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.RedundantCityName;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.neo4j.driver.*;
import org.neo4j.driver.internal.types.InternalTypeSystem;
import org.neo4j.driver.types.TypeSystem;
import org.neo4j.driver.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static org.neo4j.driver.internal.types.InternalTypeSystem.*;

@Slf4j
public class Database {

    private Driver driver;

    public Database() {
        this.driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123"));
    }

    public boolean saveGraph(Country country) {
        try(Session session = driver.session()) {
            session.writeTransaction(tx -> {
                for (City city : country.getCities()) { //TODO: tworzenie jako jeden query
                    String query = "CREATE (:Node {graphName: $graphName, name: $nodeName})";
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("graphName", country.getName());
                    parameters.put("nodeName", city.getName());
                    tx.run(query, parameters);
                }
                Set<City> pathsCreated = new HashSet<>();
                for (City origin : country.getCities()) {
                    pathsCreated.add(origin);
                    origin.getDirections().forEach((destination, road) -> {
                        if (pathsCreated.contains(destination)) return;
                        String query = "MATCH (origin: Node {graphName: $graphName, name: $originName})" +
                                "MATCH (dest: Node {graphName: $graphName, name: $destName})" +
                                "CREATE (origin) -[:PATH{name: $roadName, distance: $distance, pheromone: " +
                                "$pheromone}]-> " +
                                "(dest)";
                        Map<String, Object> parameters = new HashMap<>();
                        parameters.put("graphName", country.getName());
                        parameters.put("originName", origin.getName());
                        parameters.put("destName", destination.getName());
                        parameters.put("roadName", road.getName());
                        parameters.put("distance", road.getDistance());
                        parameters.put("pheromone", road.getPheromone());
                        tx.run(query, parameters);
                    });
                }
                return true;
            });
        }
        return true;
    }

    public Country getGraph(String name) {
        try(Session session = driver.session()) {
            return session.writeTransaction(tx -> {
                String query = "Match (n:Node {graphName: $graphName})-[r:PATH]->(m:Node {graphName: $graphName})\n" +
                        "Return n,r,m";
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("graphName", name);
                Result result = tx.run(query, parameters);
                List<Record> list = result.list();
                return createGraph(list);
            });
        }
    }

    /**
     * Record field:
     * origin - relation - destination
     * @param records - rekordy z bazy danych
     * @return graf stworzony na podstawie rekordów
     */
    private Country createGraph(List<Record> records) {
        Country country = null;
        for (Record record : records) {
            List<Pair<String, Value>> fields = record.fields();
            if (fields.size() == 3) {
                Map<String, Object> origin = fields.get(0).value().asMap();
                Map<String, Object> path = fields.get(1).value().asMap();
                Map<String, Object> destination = fields.get(2).value().asMap();
                if (country == null) {
                    String graphName = (String) origin.get("graphName");
                    country = new Country(graphName);
                }

                City org = country.createCity((String) origin.get("name"));
                City dest = country.createCity((String) destination.get("name"));

                double distance = (double) path.get("distance");
                double pheromone = (double) path.get("pheromone");
                try {
                    country.addEdge(org, dest, distance, pheromone);
                } catch (RedundantCityName redundantCityName) {
                    log.info(redundantCityName.getLocalizedMessage());
                }
            }
        }
        return country;
    }

    public List<String> getGraphsNames() {
        try(Session session = driver.session()) {
            return session.writeTransaction(tx -> {
                String query = "MATCH (n: Node) WHERE exists(n.graphName) WITH DISTINCT n.graphName as GN return GN";
                Result result = tx.run(query);
                 return result.list().stream().map(record -> {
                    List<Pair<String, Value>> fields = record.fields();
                    Optional<Pair<String, Value>> first = fields.stream().findFirst();
                    if (first.isPresent()) {
                        return first.get().value().asString();
                    }
                    return "";
                }).filter(s -> !s.isBlank())
                .collect(Collectors.toList());
            });
        }
    }
}
