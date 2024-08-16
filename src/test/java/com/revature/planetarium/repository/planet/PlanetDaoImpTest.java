package com.revature.planetarium.repository.planet;

import com.revature.planetarium.Utility;
import com.revature.planetarium.entities.Planet;
import com.revature.planetarium.entities.User;
import com.revature.planetarium.repository.user.UserDao;
import com.revature.planetarium.repository.user.UserDaoImp;
import org.junit.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class PlanetDaoImpTest {
    private PlanetDao planetDao;
    private Planet createPlanetPositive;
    private Planet readPlanetPositive;

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
    }

    @Test
    public void createPlanetPositive() {
        Optional<Planet> returnedPlanet = planetDao.createPlanet(createPlanetPositive);
        returnedPlanet.ifPresent(planet -> assertSame(createPlanetPositive, planet));
    }

    @Test
    public void createPlanetNegative() {
        Planet negativePLanet = new Planet();
        Optional<Planet> returnedPlanet = planetDao.createPlanet(new Planet());
        returnedPlanet.ifPresent(planet -> assertSame(negativePLanet, planet));
    }


    @Test
    public void readPlanetIntPositive() {
        Optional<Planet> returnedPlanet = planetDao.readPlanet(1);
        returnedPlanet.ifPresent(planet -> assertEquals(readPlanetPositive, planet));
    }

    @Test
    public void readPlanetStringPositive() {
        Optional<Planet> returnedPlanet = planetDao.readPlanet("Earth");
        returnedPlanet.ifPresent(planet -> assertEquals(readPlanetPositive, planet));
    }

    @Test
    public void readAllPlanets() {
        List<Planet> allPlanets = planetDao.readAllPlanets();
        assertFalse(allPlanets.isEmpty());
    }

    @Test
    public void readPlanetsByOwner() {
        List<Planet> allPlanetsByOwner = planetDao.readPlanetsByOwner(1);
        assertFalse(allPlanetsByOwner.isEmpty());
    }

    @Test
    public void updatePlanet() {
    }

    @Test
    public void deletePlanetInt() {
    }

    @Test
    public void deletePlanetString() {
    }
}