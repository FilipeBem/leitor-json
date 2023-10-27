package br.edu.unifalmg.repository.impl;

import br.edu.unifalmg.domain.Chore;
import br.edu.unifalmg.repository.ChoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import java.io.*;
import java.util.*;


public class ChoreRepositoryImpl implements ChoreRepository {
    private final String FILENAME = "chores.json";

    public void save(Chore chore) {
        try {
            FileOutputStream fos = new FileOutputStream(FILENAME, true);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(chore);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Chore findById(int id) {
        try {
            FileInputStream fis = new FileInputStream(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Chore chore;
            while (fis.available() > 0) {
                chore = (Chore) ois.readObject();
                if (chore.getId() == id) {
                    ois.close();
                    fis.close();
                    return chore;
                }
            }
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Chore chore) {
        try {
            FileInputStream fis = new FileInputStream(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<Chore> chores = new ArrayList<>();
            Chore c;
            while (fis.available() > 0) {
                c = (Chore) ois.readObject();
                if (c.getId() == chore.getId()) {
                    chores.add(chore);
                } else {
                    chores.add(c);
                }
            }
            ois.close();
            fis.close();
            FileOutputStream fos = new FileOutputStream(FILENAME);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Chore ch : chores) {
                oos.writeObject(ch);
            }
            oos.close();
            fos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            FileInputStream fis = new FileInputStream(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<Chore> chores = new ArrayList<>();
            Chore c;
            while (fis.available() > 0) {
                c = (Chore) ois.readObject();
                if (c.getId() != id) {
                    chores.add(c);
                }
            }
            ois.close();
            fis.close();
            FileOutputStream fos = new FileOutputStream(FILENAME);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Chore ch : chores) {
                oos.writeObject(ch);
            }
            oos.close();
            fos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

public class FileChoreRepository implements ChoreRepository {

    private ObjectMapper mapper;

    public FileChoreRepository() {
        mapper = new ObjectMapper().findAndRegisterModules();
    }

    @Override
    public List<Chore> load() {
        try {
            // Arrays.asList → Gera uma lista IMUTAVEL
            return new ArrayList<>(
                    Arrays.asList(
                            mapper.readValue(new File("chores.json"), Chore[].class)
                    )
            );

            // Using TypeReference
//            return mapper.readValue(new File("chores.json"),
//                    new TypeReference<>() {
//                    });
        } catch(MismatchedInputException exception) {
            System.out.println("Unable to convert the content of the file into Chores!");
        } catch(IOException exception) {
            System.out.println("ERROR: Unable to open file.");
        }
        return new ArrayList<>();
    }

    @Override
    public boolean save(List<Chore> chores) {
        try {
            mapper.writeValue(new File("chores.json"), chores);
            return true;
        } catch (IOException exception) {
            System.out.println("ERROR: Unable to write the chores on the file.");
        }
        return false;
    }

}