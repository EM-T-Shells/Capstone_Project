package com.revature.planetarium.repository.moon;

import com.revature.planetarium.Utility;
import com.revature.planetarium.entities.Moon;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class MoonDaoImpTest {

    private MoonDao moonDao;
    private Moon nonExistingMoon, existingMoon;

    @Before
    public void setUp() throws Exception {

        Utility.resetTestDatabase();

        moonDao = new MoonDaoImp();

        nonExistingMoon =  new Moon();
        nonExistingMoon.setMoonId(3);
        nonExistingMoon.setMoonName("Artemis");
        nonExistingMoon.setOwnerId(1);
        String imagePath = "src/test/resources/Celestial-Images/moon-1.jpg";
        Path path = Paths.get(imagePath);
        byte[] imageFile = Files.readAllBytes(path);
        String image = Base64.getEncoder().encodeToString(imageFile);
        nonExistingMoon.setImageData(image);

        existingMoon = new Moon();
        existingMoon.setMoonId(1);
        existingMoon.setMoonName("Luna");
        existingMoon.setOwnerId(1);
    }

    @After
    public void tearDown() {
        Utility.resetTestDatabase();
    }

    @Test
    public void createMoon() {
        Optional<Moon> returnedMoonOptional = moonDao.createMoon(nonExistingMoon);
        if(returnedMoonOptional.isEmpty()){
            Assert.fail();
        }
        Moon returnedMoon = returnedMoonOptional.get();
        Assert.assertSame(nonExistingMoon, returnedMoon);
    }

    @Test
    public void readMoonPositive() {
        Optional<Moon> returnedMoonOptional = moonDao.readMoon(existingMoon.getMoonId());
        if(returnedMoonOptional.isEmpty()){
            Assert.fail();
        }
        Moon returnedMoon = returnedMoonOptional.get();
        Assert.assertEquals(existingMoon.getMoonId(), returnedMoon.getMoonId());
    }

    @Test
    public void readMoonNegative() {
        Optional<Moon> returnedMoonOptional = moonDao.readMoon(nonExistingMoon.getMoonId());
        Assert.assertFalse(returnedMoonOptional.isPresent());
    }

    @Test
    public void testReadMoonPositive() {
        Optional<Moon> returnedMoonOptional = moonDao.readMoon(existingMoon.getMoonName());
        if(returnedMoonOptional.isEmpty()){
            Assert.fail();
        }
        Moon returnedMoon = returnedMoonOptional.get();
        Assert.assertEquals(existingMoon.getMoonName(), returnedMoon.getMoonName());
    }

    @Test
    public void testReadMoonNegative() {
        Optional<Moon> returnedMoonOptional = moonDao.readMoon(nonExistingMoon.getMoonName());
        Assert.assertFalse(returnedMoonOptional.isPresent());
    }

    @Test
    public void readAllMoons() {
        List<Moon> moonList = moonDao.readAllMoons();
        Assert.assertTrue( moonList.size() >= 2);
    }

    @Test
    public void readMoonsByPlanetPositive() {
        List<Moon> moonList = moonDao.readMoonsByPlanet(existingMoon.getOwnerId());
        Assert.assertEquals(1, moonList.size());
    }

    @Test
    public void readMoonsByPlanetNegative() {
        nonExistingMoon.setOwnerId(3);
        List<Moon> moonList = moonDao.readMoonsByPlanet(nonExistingMoon.getOwnerId());
        Assert.assertEquals(0, moonList.size());
    }

    @Test
    public void updateMoonPositive() {
        Moon updateMoon = new Moon(existingMoon.getMoonId(), nonExistingMoon.getMoonName(), nonExistingMoon.getOwnerId());
        Optional<Moon> updatedMoonOptional = moonDao.updateMoon(updateMoon);
        if(updatedMoonOptional.isEmpty()){
            Assert.fail();
        }
        Moon updatedMoon = updatedMoonOptional.get();
        Assert.assertSame(updateMoon, updatedMoon);
    }

    @Test
    public void updateMoonNegative() {
        Optional<Moon> updatedMoonOptional = moonDao.updateMoon(nonExistingMoon);
        Assert.assertFalse(updatedMoonOptional.isPresent());
    }

    @Test
    public void deleteMoonPositive() {
        Assert.assertTrue(moonDao.deleteMoon(existingMoon.getMoonId()));
    }

    @Test
    public void deleteMoonNegative() {
        Assert.assertFalse(moonDao.deleteMoon(nonExistingMoon.getMoonId()));
    }

    @Test
    public void testDeleteMoonPositive() {
        Assert.assertTrue(moonDao.deleteMoon(existingMoon.getMoonName()));
    }

    @Test
    public void testDeleteMoonNegative() {
        Assert.assertFalse(moonDao.deleteMoon(nonExistingMoon.getMoonName()));
    }
}