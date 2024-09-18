package com.example.itec3860_project2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.coyote.Request;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class VehicleController {

    private final ObjectMapper mapper = new ObjectMapper();
    private ApplicationHome home = new ApplicationHome(Itec3860Project2Application.class);


    @RequestMapping(value="/addVehicle", method= RequestMethod.POST)
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) throws IOException {
        // we will keep track of vehicles in the file "vehicles.txt"
        // serialize the vehicle
        String json = mapper.writeValueAsString(vehicle);
        //File file = new ClassPathResource("vehicles.txt").getFile();
        File file = new File(home.getDir() + "/vehicles.txt");
        FileUtils.writeStringToFile(file, json +"\n", "UTF-8", true);
        return vehicle;
    }


    @RequestMapping(value="/getVehicle/{id}", method=RequestMethod.GET)
    public Vehicle getVehicle(@PathVariable int id) throws IOException {
        File file = new File(home.getDir() + "/vehicles.txt");
        List<String> lines = FileUtils.readLines(file, "UTF-8");
        for(String line : lines){
            Vehicle v = mapper.readValue(line, Vehicle.class);
            if(v.getId() == id){
                return v;
            }
        }
        return new Vehicle(0,"",0,0);
    }

    @RequestMapping(value="/deleteVehicle/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<String> deleteVehicle(@PathVariable int id) throws IOException {
        File file = new File(home.getDir() + "/vehicles.txt");
        List<String> lines = FileUtils.readLines(file, "UTF-8");
        List<String> newlines = new ArrayList<>();
        boolean found = false;
        for(String line : lines){
            Vehicle v = mapper.readValue(line, Vehicle.class);
            if(v.getId() != id){
                newlines.add(line);
            } else {
                found = true;
            }
        }
        FileUtils.writeLines(file, newlines, "\n");
        if(found){
            return new ResponseEntity<>(HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value="/updateVehicle", method=RequestMethod.PUT)
    public Vehicle updateVehicle(@RequestBody Vehicle vehicle) throws IOException {
        //read the file
        File file = new File(home.getDir() + "/vehicles.txt");
        List<String> lines = FileUtils.readLines(file, "UTF-8");
        String output = "";
        for(String line : lines){
            Vehicle v = mapper.readValue(line, Vehicle.class);
            if(v.getId() == vehicle.getId()) {
                v.setMakeModel(vehicle.getMakeModel());
                v.setYear(vehicle.getYear());
                v.setRetailPrice(vehicle.getRetailPrice());
                String newLine = mapper.writeValueAsString(v);
                output = output + newLine + "\n";
            } else {
                output = output + line + "\n";
            }
        }
        FileUtils.writeStringToFile(file, output, "UTF-8");
        return vehicle;
    }


    @RequestMapping(value="/printVars", method=RequestMethod.GET)
    public void printVars() throws IOException {
//        home.getDir();    // returns the folder where the jar is. This is what I wanted.
//        home.getSource(); // returns the jar absolute path.
        System.out.println(home.getDir());
        //System.out.println(home.getSource());
    }
}
