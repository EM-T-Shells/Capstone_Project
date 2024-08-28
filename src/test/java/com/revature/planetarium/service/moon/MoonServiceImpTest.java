package com.revature.planetarium.service.moon;

import com.revature.planetarium.Utility;
import com.revature.planetarium.entities.Moon;
import com.revature.planetarium.repository.moon.MoonDao;
import com.revature.planetarium.repository.moon.MoonDaoImp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MoonServiceImpTest {

    private MoonDao moonDao;
    private MoonService moonService;
    private Moon moonName30Long, moonName1Long, moonNoName, moonNameTooLong, existingMoon1, existingMoon2,
            updateMoon1, updateMoon2, updateMoon3;

    @Before
    public void setUp() throws Exception {

        Utility.resetTestDatabase();

        String imagePath = "src/test/resources/Celestial-Images/moon-1.jpg";
        Path path = Paths.get(imagePath);
        byte[] imageFile = Files.readAllBytes(path);
        String image = Base64.getEncoder().encodeToString(imageFile);

        moonName30Long = new Moon();
        moonName30Long.setMoonId(3);
        moonName30Long.setMoonName("MoonNameIsThirtyCharactersLong");
        moonName30Long.setOwnerId(1);
        moonName30Long.setImageData(image);

        moonName1Long = new Moon();
        moonName1Long.setMoonId(3);
        moonName1Long.setMoonName("M");
        moonName1Long.setOwnerId(1);
        moonName1Long.setImageData(image);

        moonNoName = new Moon();
        moonNoName.setMoonId(3);
        moonNoName.setMoonName("");
        moonNoName.setOwnerId(1);
        moonNoName.setImageData(image);

        moonNameTooLong = new Moon();
        moonNameTooLong.setMoonId(3);
        moonNameTooLong.setMoonName("MoonNameThirtyOneCharactersLong");
        moonNameTooLong.setOwnerId(1);
        moonNameTooLong.setImageData(image);

        existingMoon1 = new Moon();
        existingMoon1.setMoonId(1);
        existingMoon1.setMoonName("Luna");
        existingMoon1.setOwnerId(1);
        existingMoon1.setImageData(image);

        existingMoon2 = new Moon();
        existingMoon2.setMoonId(2);
        existingMoon2.setMoonName("Titan");
        existingMoon2.setOwnerId(2);
        existingMoon2.setImageData(image);

        updateMoon1 = new Moon();
        updateMoon1.setMoonId(1);
        updateMoon1.setMoonName("Artemis");
        updateMoon1.setOwnerId(1);
        updateMoon1.setImageData(image);

        updateMoon2 = new Moon();
        updateMoon2.setMoonId(1);
        updateMoon2.setMoonName("MoonNameThirtyOneCharactersLong");
        updateMoon2.setOwnerId(1);
        updateMoon2.setImageData(image);

        updateMoon3 = new Moon();
        updateMoon3.setMoonId(1);
        updateMoon3.setMoonName("Titan");
        updateMoon3.setOwnerId(1);
        updateMoon3.setImageData(image);

        moonDao = Mockito.mock(MoonDaoImp.class);
        moonService = new MoonServiceImp(moonDao);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createMoon30Long() {
        Mockito.when(moonDao.readMoon(moonName30Long.getMoonName())).thenReturn(Optional.empty());
        Mockito.when(moonDao.createMoon(moonName30Long)).thenReturn(Optional.of(moonName30Long));
        Moon returnedMoon = moonService.createMoon(moonName30Long);
        Assert.assertSame(moonName30Long, returnedMoon);
    }

    @Test
    public void createMoon1Long() {
        Mockito.when(moonDao.readMoon(moonName1Long.getMoonName())).thenReturn(Optional.empty());
        Mockito.when(moonDao.createMoon(moonName1Long)).thenReturn(Optional.of(moonName1Long));
        Moon returnedMoon = moonService.createMoon(moonName1Long);
        Assert.assertSame(moonName1Long, returnedMoon);
    }

    @Test
    public void createMoonNoName() {
        Mockito.when(moonDao.readMoon(moonNoName.getMoonName())).thenReturn(Optional.empty());
        Mockito.when(moonDao.createMoon(moonNoName)).thenReturn(Optional.of(moonNoName));
        RuntimeException exception = Assert.assertThrows(RuntimeException.class, () -> { moonService.createMoon(moonNoName);});
        Assert.assertEquals("Moon name must be between 1 and 30 characters", exception.getMessage());
    }

    @Test
    public void createMoonNameTooLong() {
        Mockito.when(moonDao.readMoon(moonNameTooLong.getMoonName())).thenReturn(Optional.empty());
        Mockito.when(moonDao.createMoon(moonNameTooLong)).thenReturn(Optional.of(moonNameTooLong));
        RuntimeException exception = Assert.assertThrows(RuntimeException.class, () -> { moonService.createMoon(moonNameTooLong);});
        Assert.assertEquals("Moon name must be between 1 and 30 characters", exception.getMessage());
    }

    @Test
    public void createMoonExistingName() {
        Mockito.when(moonDao.readMoon(existingMoon1.getMoonName())).thenReturn(Optional.of(existingMoon1));
        Mockito.when(moonDao.createMoon(existingMoon1)).thenReturn(Optional.of(existingMoon1));
        RuntimeException exception = Assert.assertThrows(RuntimeException.class, () -> { moonService.createMoon(existingMoon1);});
        Assert.assertEquals("Moon name must be unique", exception.getMessage());
    }

    @Test
    public void createMoonException() {
        Mockito.when(moonDao.readMoon(moonName30Long.getMoonName())).thenReturn(Optional.empty());
        Mockito.when(moonDao.createMoon(moonName30Long)).thenReturn(Optional.empty());
        RuntimeException exception = Assert.assertThrows(RuntimeException.class, () -> { moonService.createMoon(moonName30Long);});
        Assert.assertEquals("Could not create new moon", exception.getMessage());
    }

    @Test
    public void selectMoonById() {
        Mockito.when(moonDao.readMoon(existingMoon1.getMoonId())).thenReturn(Optional.of(existingMoon1));
        Moon returnedMoon = moonService.selectMoon(existingMoon1.getMoonId());
        Assert.assertSame(existingMoon1, returnedMoon);
    }

    @Test
    public void selectMoonByName() {
        Mockito.when(moonDao.readMoon(existingMoon1.getMoonName())).thenReturn(Optional.of(existingMoon1));
        Moon returnedMoon = moonService.selectMoon(existingMoon1.getMoonName());
        Assert.assertSame(existingMoon1, returnedMoon);
    }

    @Test
    public void selectMoonException() {
        Mockito.when(moonDao.readMoon(existingMoon1.getMoonId())).thenReturn(Optional.of(existingMoon1));
        RuntimeException exception = Assert.assertThrows(RuntimeException.class, () -> { moonService.selectMoon(0.0f);});
        Assert.assertEquals("Identifier must be an Integer or String", exception.getMessage());
    }

    @Test
    public void selectMoonNotFound() {
        Mockito.when(moonDao.readMoon(moonName30Long.getMoonId())).thenReturn(Optional.empty());
        RuntimeException exception = Assert.assertThrows(RuntimeException.class, () -> { moonService.selectMoon(moonName30Long.getMoonId());});
        Assert.assertEquals("Moon not found", exception.getMessage());
    }

    @Test
    public void selectAllMoons() {
        List<Moon> moonList = new LinkedList<>();
        moonList.add(existingMoon1);
        moonList.add(existingMoon2);
        Mockito.when(moonDao.readAllMoons()).thenReturn(moonList);
        List<Moon> returnedList = moonService.selectAllMoons();
        Assert.assertSame(moonList, returnedList);
    }

    @Test
    public void selectByPlanet() {
        List<Moon> moonList = new LinkedList<>();
        moonList.add(existingMoon1);
        Mockito.when(moonDao.readMoonsByPlanet(existingMoon1.getOwnerId())).thenReturn(moonList);
        List<Moon> returnedList = moonService.selectByPlanet(existingMoon1.getOwnerId());
        Assert.assertSame(moonList, returnedList);
    }

    @Test
    public void updateMoon() {
        Mockito.when(moonDao.readMoon(updateMoon1.getMoonId())).thenReturn(Optional.of(existingMoon1));
        Mockito.when(moonDao.readMoon(updateMoon1.getMoonName())).thenReturn(Optional.empty());
        Mockito.when(moonDao.updateMoon(updateMoon1)).thenReturn(Optional.of(updateMoon1));
        Moon returnedMoon = moonService.updateMoon(updateMoon1);
        Assert.assertSame(updateMoon1, returnedMoon);
    }

    @Test
    public void updateMoonNoMoon() {
        Mockito.when(moonDao.readMoon(3)).thenReturn(Optional.empty());
        Mockito.when(moonDao.readMoon(updateMoon1.getMoonName())).thenReturn(Optional.empty());
        Mockito.when(moonDao.updateMoon(updateMoon1)).thenReturn(Optional.of(updateMoon1));
        RuntimeException exception = Assert.assertThrows( RuntimeException.class, () -> {moonService.updateMoon(updateMoon1);});
        Assert.assertEquals("Moon not found, could not update", exception.getMessage());
    }

    @Test
    public void updateMoonNameException() {
        Mockito.when(moonDao.readMoon(updateMoon2.getMoonId())).thenReturn(Optional.of(existingMoon1));
        Mockito.when(moonDao.readMoon(updateMoon2.getMoonName())).thenReturn(Optional.empty());
        Mockito.when(moonDao.updateMoon(updateMoon2)).thenReturn(Optional.of(updateMoon2));
        RuntimeException exception = Assert.assertThrows( RuntimeException.class, () -> {moonService.updateMoon(updateMoon2);});
        Assert.assertEquals("Moon name must be between 1 and 30 characters, could not update", exception.getMessage());
    }

    @Test
    public void updateMoonSameNameException() {
        Mockito.when(moonDao.readMoon(updateMoon3.getMoonId())).thenReturn(Optional.of(existingMoon1));
        Mockito.when(moonDao.readMoon(updateMoon3.getMoonName())).thenReturn(Optional.of(existingMoon2));
        Mockito.when(moonDao.updateMoon(updateMoon3)).thenReturn(Optional.of(updateMoon3));
        RuntimeException exception = Assert.assertThrows( RuntimeException.class, () -> {moonService.updateMoon(updateMoon3);});
        Assert.assertEquals("Moon name must be unique, could not update", exception.getMessage());
    }

    @Test
    public void updateMoonException() {
        Mockito.when(moonDao.readMoon(updateMoon1.getMoonId())).thenReturn(Optional.of(existingMoon1));
        Mockito.when(moonDao.readMoon(updateMoon1.getMoonName())).thenReturn(Optional.empty());
        Mockito.when(moonDao.updateMoon(updateMoon1)).thenReturn(Optional.empty());
        RuntimeException exception = Assert.assertThrows( RuntimeException.class, () -> {moonService.updateMoon(updateMoon1);});
        Assert.assertEquals("Moon update failed, please try again", exception.getMessage());
    }

    @Test
    public void deleteMoonById() {
        Mockito.when(moonDao.deleteMoon(existingMoon1.getMoonId())).thenReturn(true);
        String result = moonService.deleteMoon(existingMoon1.getMoonId());
        Assert.assertEquals("Moon deleted successfully", result);
    }

    @Test
    public void deleteMoonByName() {
        Mockito.when(moonDao.deleteMoon(existingMoon1.getMoonName())).thenReturn(true);
        String result = moonService.deleteMoon(existingMoon1.getMoonName());
        Assert.assertEquals("Moon deleted successfully", result);
    }

    @Test
    public void deleteMoonIdentifierException() {
        Mockito.when(moonDao.deleteMoon(existingMoon1.getMoonName())).thenReturn(true);
        RuntimeException exception = Assert.assertThrows(RuntimeException.class, () -> {moonService.deleteMoon(0.0f);});
        Assert.assertEquals("Identifier must be an Integer or String", exception.getMessage());
    }

    @Test
    public void deleteMoonException() {
        Mockito.when(moonDao.deleteMoon(existingMoon1.getMoonName())).thenReturn(false);
        RuntimeException exception = Assert.assertThrows(RuntimeException.class, () -> {moonService.deleteMoon(existingMoon1.getMoonName());});
        Assert.assertEquals("Moon delete failed, please try again", exception.getMessage());
    }

}