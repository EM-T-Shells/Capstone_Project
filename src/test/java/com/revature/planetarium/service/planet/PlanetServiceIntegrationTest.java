package com.revature.planetarium.service.planet;

import com.revature.planetarium.Utility;
import com.revature.planetarium.entities.Planet;
import com.revature.planetarium.exceptions.PlanetFail;
import com.revature.planetarium.repository.planet.PlanetDao;
import com.revature.planetarium.repository.planet.PlanetDaoImp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class PlanetServiceIntegrationTest {
    private static Planet validPlanet;
    private static Planet tooLongPlanet;
    private static Planet nonUniquePlanet;
    private static Planet existingPlanet;
    private static Planet updatePlanet;
    private static PlanetService planetService;

    @BeforeClass
    public static void setup() {
        // Set up planets for PlanetService
        // validPlanet for Positive Scenarios
        validPlanet = new Planet();
        validPlanet.setPlanetName("Furina");
        validPlanet.setPlanetId(3);
        validPlanet.setOwnerId(1);
        // Invalid Planets for Negative Scenarios
        tooLongPlanet = new Planet();
        tooLongPlanet.setPlanetName("Secret Art: Musou Shinsetsu!!!!");
        tooLongPlanet.setPlanetId(4);
        tooLongPlanet.setOwnerId(1);
        nonUniquePlanet = new Planet();
        nonUniquePlanet.setPlanetName("Mars");
        nonUniquePlanet.setPlanetId(5);
        nonUniquePlanet.setOwnerId(1);
        existingPlanet = new Planet();
        existingPlanet.setPlanetName("Earth");
        existingPlanet.setPlanetId(1);
        existingPlanet.setOwnerId(1);
        updatePlanet = new Planet();
        updatePlanet.setPlanetName("Fischl");
        updatePlanet.setPlanetId(1);
        updatePlanet.setOwnerId(1);
        PlanetDao planetDao = new PlanetDaoImp();
        planetService = new PlanetServiceImp(planetDao);
    }

    @Before
    public void resetDB() {
        // Reset DB before each test
        Utility.resetTestDatabase();
    }

    // Positive Scenario: Create planet with valid planet name
    @Test
    public void testPositiveScenarioCreatePlanet() {
        // Call createPlanet() and check return Object
        Planet result = planetService.createPlanet(validPlanet);
        Assert.assertNotNull(result);
        Assert.assertEquals(validPlanet.getPlanetName(), result.getPlanetName());
        Assert.assertEquals(validPlanet.getOwnerId(), result.getOwnerId());
        Assert.assertEquals(validPlanet.getPlanetId(), result.getPlanetId());
    }

    // Negative Scenario: Create planet with invalid planet name (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioCreatePlanetInvalidPlanetName() {
        Assert.assertThrows(PlanetFail.class, () -> {
            planetService.createPlanet(tooLongPlanet);
        });
    }

    // Negative Scenario: Create planet with non-unique planet name (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioCreatePlanetNonUniquePlanetName() {
        Assert.assertThrows(PlanetFail.class, () -> {
            planetService.createPlanet(nonUniquePlanet);
        });
    }

    // Positive Scenario: Select planet by existing planet id
    @Test
    public void testPositiveScenarioSelectPlanetById() {
        Planet result = planetService.selectPlanet(existingPlanet.getPlanetId());
        Assert.assertEquals(existingPlanet.getPlanetName(), result.getPlanetName());
        Assert.assertEquals(existingPlanet.getPlanetId(), result.getPlanetId());
        Assert.assertEquals(existingPlanet.getOwnerId(), result.getOwnerId());
    }

    // Positive Scenario: Select planet by existing planet name
    @Test
    public void testPositiveScenarioSelectPlanetByName() {
        Planet result = planetService.selectPlanet(existingPlanet.getPlanetName());
        Assert.assertEquals(existingPlanet.getPlanetName(), result.getPlanetName());
        Assert.assertEquals(existingPlanet.getPlanetId(), result.getPlanetId());
        Assert.assertEquals(existingPlanet.getOwnerId(), result.getOwnerId());
    }

    // Positive Scenario: Select a nonexistent planet (Throw PlanetFail Exception)
    @Test
    public void testPositiveScenarioSelectNonexistentPlanet() {
        Assert.assertThrows(PlanetFail.class, () -> {
            planetService.selectPlanet(validPlanet.getPlanetName());
        });
    }

    // Negative Scenario: Select planet by invalid identifier (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioSelectPlanetByInvalidIdentifier() {
        double invalidDouble = 1.1;
        Assert.assertThrows(PlanetFail.class, () -> {
            planetService.selectPlanet(invalidDouble);
        });
        boolean invalidBool = false;
        Assert.assertThrows(PlanetFail.class, () -> {
            planetService.selectPlanet(invalidBool);
        });
    }

    // Positive Scenario: Select all planets in the planetarium
    @Test
    public void testSelectAllPlanets() {
        List<Planet> result = planetService.selectAllPlanets();
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(existingPlanet));
    }

    // Positive Scenario: Select all planets by owner id
    @Test
    public void testSelectByOwner() {
        List<Planet> result = planetService.selectByOwner(1);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(existingPlanet));
    }

    // Positive Scenario: Update existing planet in the planetarium
    @Test
    public void testPositiveScenarioUpdatePlanet() {
        Planet result = planetService.updatePlanet(updatePlanet);
        Assert.assertEquals(updatePlanet.getPlanetName(), result.getPlanetName());
        Assert.assertEquals(updatePlanet.getPlanetId(), result.getPlanetId());
        Assert.assertEquals(updatePlanet.getOwnerId(), result.getOwnerId());
    }

    // Negative Scenario: Update existing planet with invalid planet name (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioUpdateInvalidPlanetName() {
        Assert.assertThrows(PlanetFail.class, () -> {
            planetService.updatePlanet(tooLongPlanet);
        });
    }

    // Negative Scenario: Update planet with non-unique planet name (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioNonUniquePlanetName() {
        Assert.assertThrows(PlanetFail.class, () -> {
            planetService.updatePlanet(nonUniquePlanet);
        });
    }

    // Negative Scenario: Update nonexistent planet (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioUpdateNonexistentPlanet() {
        Assert.assertThrows(PlanetFail.class, () -> {
            planetService.updatePlanet(validPlanet);
        });
    }

    // Positive Scenario: Delete planet by existing planet id
    @Test
    public void testPositiveScenarioDeletePlanetById() {
        String result = planetService.deletePlanet(existingPlanet.getPlanetId());
        Assert.assertEquals("Planet deleted successfully", result);
    }

    // Positive Scenario: Delete planet by existing planet name
    @Test
    public void testPositiveScenarioDeletePlanetByName() {
        String result = planetService.deletePlanet(existingPlanet.getPlanetName());
        Assert.assertEquals("Planet deleted successfully", result);
    }

    // Negative Scenario: Delete nonexistent planet from planetarium (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioDeleteNonexistentPlanet() {
        Assert.assertThrows(PlanetFail.class, () -> {
            planetService.deletePlanet(validPlanet.getPlanetName());
        });
    }

    // Negative Scenario: Delete planet by invalid identifier (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioDeletePlanetByInvalidIdentifier() {
        Assert.assertThrows(PlanetFail.class, () -> {
            planetService.deletePlanet(1.1);
        });
        Assert.assertThrows(PlanetFail.class, () -> {
            planetService.deletePlanet(false);
        });
    }
}