package com.revature.planetarium.repository.planet;

import com.revature.planetarium.Utility;
import com.revature.planetarium.entities.Planet;
import com.revature.planetarium.exceptions.PlanetFail;
import com.revature.planetarium.utility.DatabaseConnector;
import org.junit.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class PlanetDaoImpTest {
    private PlanetDao planetDao;
    private Planet createPlanetPositive, createPlanetNegative, readPlanetPositive;

    private String imageData;

    public PlanetDaoImpTest() throws IOException {
    }

    @BeforeClass
    public static void setUpTestDb() throws Exception {
        Utility.resetTestDatabase();
    }

    @Before
    public void setup() throws Exception {
        planetDao = new PlanetDaoImp();
        createPlanetPositive = new Planet();
        createPlanetPositive.setPlanetId(1);
        createPlanetPositive.setPlanetName("Planet");
        createPlanetPositive.setOwnerId(1);
        imageData = Utility.convertToBase64(String.format("src/test/resources/Celestial-Images/%s-%d.jpg", "planet", 1));
        createPlanetPositive.setImageData(imageData);
        readPlanetPositive = new Planet();
        readPlanetPositive.setPlanetId(1);
        readPlanetPositive.setPlanetName("Earth");
        readPlanetPositive.setOwnerId(1);
    }

    @After
    public void tearDown() throws Exception {
        Utility.resetTestDatabase();
    }

    @Test
    public void createPlanetPositive() {
        Optional<Planet> returnedPlanet = planetDao.createPlanet(createPlanetPositive);
        returnedPlanet.ifPresent(planet -> assertSame(createPlanetPositive, planet));
    }

    @Test
    public void createPlanetNegative() {
        Planet negativePlanet = new Planet();
        Assert.assertThrows(PlanetFail.class, () -> {
            planetDao.createPlanet(negativePlanet);
        });
    }


    @Test
    public void readPlanetIntPositive() {
        Optional<Planet> returnedPlanet = planetDao.readPlanet(1);
        returnedPlanet.ifPresent(planet -> assertEquals(readPlanetPositive, planet));
    }

    @Test
    public void readPlanetIntNegative() {
        Optional<Planet> emptyOptional = Optional.empty();
        Optional<Planet> returnedPlanet = planetDao.readPlanet(60);
        Assert.assertEquals(returnedPlanet, emptyOptional);
    }

    @Test
    public void readPlanetStringPositive() {
        Optional<Planet> returnedPlanet = planetDao.readPlanet("Earth");
        returnedPlanet.ifPresent(planet -> assertEquals(readPlanetPositive, planet));
    }
    @Test
    public void readPlanetStringNegative() {
        Optional<Planet> emptyOptional = Optional.empty();
        Optional<Planet> returnedPlanet = planetDao.readPlanet("Nonexistent planet");
        Assert.assertEquals(returnedPlanet, emptyOptional);
    }

    @Test
    public void readAllPlanetsPositive() {
        Utility.resetTestDatabase();
        List<Planet> allPlanets = planetDao.readAllPlanets();
        assertFalse(allPlanets.isEmpty());
    }

    @Test
    public void readAllPlanetsNegative() {
        deletePlanetsForNegativeGetAllPlanets();
        List<Planet> allPlanets = planetDao.readAllPlanets();
        assertTrue(allPlanets.isEmpty());

    }

    @Test
    public void readPlanetsByOwnerPositive() {
        List<Planet> allPlanetsByOwner = planetDao.readPlanetsByOwner(1);
        assertFalse(allPlanetsByOwner.isEmpty());
    }

    @Test
    public void readPlanetsByOwnerNegative() {
        List<Planet> allPlanetsByOwner = planetDao.readPlanetsByOwner(60);
        assertTrue(allPlanetsByOwner.isEmpty());
    }


    @Test
    public void updatePlanet() {
        createPlanetPositive.setPlanetName("Updated Planet Name");
        Optional<Planet> optionalPlanet = planetDao.updatePlanet(createPlanetPositive);
        optionalPlanet.ifPresent(planet -> assertEquals(createPlanetPositive, optionalPlanet.get()));
    }

    @Test
    public void deletePlanetIntPositive() {
        Utility.resetTestDatabase();
        assertTrue(planetDao.deletePlanet(1));
    }
    
    @Test
    public void deletePlanetIntNegative() {
        assertFalse(planetDao.deletePlanet(100));
    }
    
    @Test
    public void deletePlanetStringPositive() {
        Utility.resetTestDatabase();
        assertTrue(planetDao.deletePlanet("Earth"));
    }
    
    @Test
    public void deletePlanetStringNegative() {
        assertFalse(planetDao.deletePlanet("Name"));
    }

    public void deletePlanetsForNegativeGetAllPlanets() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "delete from planets";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new AssertionError("Could not delete planets");
        }
    }
}