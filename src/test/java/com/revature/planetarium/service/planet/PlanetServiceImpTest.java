package com.revature.planetarium.service.planet;

import com.revature.planetarium.entities.Planet;
import com.revature.planetarium.exceptions.PlanetFail;
import com.revature.planetarium.repository.planet.PlanetDao;
import com.revature.planetarium.repository.planet.PlanetDaoImp;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlanetServiceImpTest {
    private static Planet validPlanet;
    private static Planet tooLongPlanet;
    private static Planet nonUniquePlanet;
    private static Planet existingPlanet;
    private static Planet updatePlanet;
    private static PlanetService planetService;

    @BeforeClass
    public static void setup() {
        // Set up mock planets to use for PlanetService
        validPlanet = Mockito.mock(Planet.class);
        Mockito.when(validPlanet.getPlanetName()).thenReturn("Furina");
        Mockito.when(validPlanet.getPlanetId()).thenReturn(2);
        Mockito.when(validPlanet.getOwnerId()).thenReturn(1);
        tooLongPlanet = Mockito.mock(Planet.class);
        Mockito.when(tooLongPlanet.getPlanetName()).thenReturn("Secret Art: Musou Shinsetsu!!!!");
        Mockito.when(tooLongPlanet.getPlanetId()).thenReturn(4);
        nonUniquePlanet = Mockito.mock(Planet.class);
        Mockito.when(nonUniquePlanet.getPlanetName()).thenReturn("Yelan");
        Planet createdPlanet = Mockito.mock(Planet.class);
        Mockito.when(createdPlanet.getPlanetName()).thenReturn("Furina");
        Mockito.when(createdPlanet.getOwnerId()).thenReturn(1);
        Mockito.when(createdPlanet.getPlanetId()).thenReturn(2);
        existingPlanet = Mockito.mock(Planet.class);
        Mockito.when(existingPlanet.getPlanetName()).thenReturn("Yelan");
        Mockito.when(existingPlanet.getPlanetId()).thenReturn(3);
        Mockito.when(existingPlanet.getOwnerId()).thenReturn(1);
        updatePlanet = Mockito.mock(Planet.class);
        Mockito.when(updatePlanet.getPlanetName()).thenReturn("Fischl");
        Mockito.when(updatePlanet.getPlanetId()).thenReturn(3);
        Mockito.when(updatePlanet.getOwnerId()).thenReturn(9);
        // Set up mock planetDao
        PlanetDao planetDao = Mockito.mock(PlanetDaoImp.class);
        planetService = new PlanetServiceImp(planetDao);
        Mockito.when(planetDao.readPlanet(validPlanet.getPlanetName())).thenReturn(Optional.empty());
        Mockito.when(planetDao.readPlanet(validPlanet.getPlanetId())).thenReturn(Optional.empty());
        Mockito.when(planetDao.readPlanet(tooLongPlanet.getPlanetId())).thenReturn(Optional.empty());
        Mockito.when(planetDao.readPlanet(nonUniquePlanet.getPlanetName())).thenReturn(Optional.of(existingPlanet));
        Mockito.when(planetDao.createPlanet(validPlanet)).thenReturn(Optional.of(createdPlanet));
        Mockito.when(planetDao.readPlanet(existingPlanet.getPlanetId())).thenReturn(Optional.of(existingPlanet));
        Mockito.when(planetDao.readPlanet(existingPlanet.getPlanetName())).thenReturn(Optional.of(existingPlanet));
        Mockito.when(planetDao.updatePlanet(updatePlanet)).thenReturn(Optional.of(updatePlanet));
        Mockito.when(planetDao.deletePlanet(existingPlanet.getPlanetId())).thenReturn(true);
        Mockito.when(planetDao.deletePlanet(existingPlanet.getPlanetName())).thenReturn(true);
        Mockito.when(planetDao.deletePlanet(validPlanet.getPlanetName())).thenReturn(false);
        List<Planet> planets = new ArrayList<>();
        planets.add(existingPlanet);
        Mockito.when(planetDao.readAllPlanets()).thenReturn(planets);
        Mockito.when(planetDao.readPlanetsByOwner(1)).thenReturn(planets);
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
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(existingPlanet));
    }

    // Positive Scenario: Select all planets by owner id
    @Test
    public void testSelectByOwner() {
        List<Planet> result = planetService.selectByOwner(1);
        Assert.assertEquals(1, result.size());
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