package com.revature.planetarium.service.planet;

import com.revature.planetarium.entities.Planet;
import com.revature.planetarium.exceptions.PlanetFail;
import com.revature.planetarium.repository.planet.PlanetDao;
import com.revature.planetarium.repository.planet.PlanetDaoImp;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class PlanetServiceImpTest {
    private static Planet validPlanet;
    private static Planet tooLongPlanet;
    private static Planet nonUniquePlanet;
    private static PlanetService planetService;

    @BeforeClass
    public static void setup() {
        // Set up mock planets to use for createPlanet()
        validPlanet = Mockito.mock(Planet.class);
        Mockito.when(validPlanet.getPlanetName()).thenReturn("Furina");
        tooLongPlanet = Mockito.mock(Planet.class);
        Mockito.when(tooLongPlanet.getPlanetName()).thenReturn("Secret Art: Musou Shinsetsu!!!!");
        nonUniquePlanet = Mockito.mock(Planet.class);
        Mockito.when(nonUniquePlanet.getPlanetName()).thenReturn("Yelan");
        Planet createdPlanet = Mockito.mock(Planet.class);
        Mockito.when(createdPlanet.getPlanetName()).thenReturn("Furina");
        Mockito.when(createdPlanet.getOwnerId()).thenReturn(1);
        Mockito.when(createdPlanet.getPlanetId()).thenReturn(2);
        // Set up mock planetDao
        PlanetDao planetDao = Mockito.mock(PlanetDaoImp.class);
        planetService = new PlanetServiceImp(planetDao);
        Mockito.when(planetDao.readPlanet(validPlanet.getPlanetName())).thenReturn(Optional.empty());
        Mockito.when(planetDao.readPlanet(nonUniquePlanet.getPlanetName())).thenReturn(Optional.of(createdPlanet));
        Mockito.when(planetDao.createPlanet(validPlanet)).thenReturn(Optional.of(createdPlanet));
    }

    // Positive Scenario: Create planet with valid planet name
    @Test
    public void testPositiveScenarioCreatePlanet() {
        // Call createPlanet() and check return Object
        Planet result = planetService.createPlanet(validPlanet);
        Assert.assertNotNull(result);
        Assert.assertEquals("Furina", result.getPlanetName());
        Assert.assertEquals(1, result.getOwnerId());
        Assert.assertEquals(2, result.getPlanetId());
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
    }

    // Positive Scenario: Select planet by existing planet name
    @Test
    public void testPositiveScenarioSelectPlanetByName() {
    }

    // Positive Scenario: Select a nonexistent planet (Throw PlanetFail Exception)
    @Test
    public void testPositiveScenarioSelectNonexistentPlanet() {
    }

    // Negative Scenario: Select planet by invalid identifier (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioSelectPlanetByInvalidIdentifier() {
    }

    // Positive Scenario: Select all planets in the planetarium
    @Test
    public void testSelectAllPlanets() {
    }

    // Positive Scenario: Select all planets by owner id
    @Test
    public void testSelectByOwner() {
    }

    // Positive Scenario: Update existing planet in the planetarium
    @Test
    public void testPositiveScenarioUpdatePlanet() {
    }

    // Negative Scenario: Update existing planet with invalid planet name (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioUpdateInvalidPlanetName() {
    }

    // Negative Scenario: Update nonexistent planet (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioUpdateNonexistentPlanet() {
    }

    // Positive Scenario: Delete planet by existing planet id
    @Test
    public void testPositiveScenarioDeletePlanetById() {
    }

    // Positive Scenario: Delete planet by existing planet name
    @Test
    public void testPositiveScenarioDeletePlanetByName() {
    }

    // Negative Scenario: Delete nonexistent planet from planetarium (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioDeleteNonexistentPlanet() {
    }

    // Negative Scenario: Delete planet by invalid identifier (Throw PlanetFail Exception)
    @Test
    public void testNegativeScenarioDeletePlanetByInvalidIdentifier() {
    }
}