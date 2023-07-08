package com.edu.hogwarts.common;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.edu.hogwarts.artifact.Artifact;
import com.edu.hogwarts.artifact.ArtifactRepository;
import com.edu.hogwarts.wizard.Wizard;
import com.edu.hogwarts.wizard.WizardRepository;

@Component
public class DatabaseDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactsDB;
    private final WizardRepository wizardsDB;

    public DatabaseDataInitializer(ArtifactRepository artifactsDB, WizardRepository wizardsDB) {
        this.artifactsDB = artifactsDB;
        this.wizardsDB = wizardsDB;
    }

    @Override
    public void run(String... args) throws Exception {
        Artifact a = new Artifact();
        a.setId("1677548525115805608");
        a.setName("Delumniator");
        a.setDescription("Deluminator is a device invented by Albus Dumbledore that resembles a wand");
        a.setImageUrl("imageUrl");

        Artifact a1 = new Artifact();
        a1.setId("1677500525115805696");
        a1.setName("Resurrection Stone");
        a1.setDescription("A resurrection stone allows the holder to bring back a loved one");
        a1.setImageUrl("imageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1632548525115805696");
        a2.setName("The Marauder's Shop");
        a2.setDescription("A map of Hogwarts created by Remus Lupin");
        a2.setImageUrl("imageUrl");

        Artifact a3 = new Artifact();
        a3.setId("1677548529615805600");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick");
        a3.setImageUrl("imageUrl");

        Artifact a4 = new Artifact();
        a4.setId("1643548525115805693");
        a4.setName("Invisibility Cloak");
        a4.setDescription("The invisibility cloak is used to make the wearer invisible");
        a4.setImageUrl("imageUrl");

        Artifact a5 = new Artifact();
        a5.setId("1877548525115805615");
        a5.setName("The Sword of Gryffindor");
        a5.setDescription("A gobin-made sword adorned with large rubbies");
        a5.setImageUrl("imageUrl");

        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Albus Dumbledore");

        w.addArtiffact(a);
        w.addArtiffact(a2);

        Wizard w1 = new Wizard();
        w1.setId(2);
        w1.setName("Harry Potter");

        w1.addArtiffact(a1);
        w1.addArtiffact(a3);

        Wizard w2 = new Wizard();
        w2.setId(3);
        w2.setName("Neville Longbottom");
        w2.addArtiffact(a5);

        // This will cascade to save artifacts asssociated with each wizard
        wizardsDB.save(w);
        wizardsDB.save(w1);
        wizardsDB.save(w2);

        // Save the artifact that was not assigned to a wizard
        artifactsDB.save(a4);

    }

}
