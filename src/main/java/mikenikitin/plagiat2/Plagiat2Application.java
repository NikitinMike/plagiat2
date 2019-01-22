package mikenikitin.plagiat2;

//import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
//@AllArgsConstructor
public class Plagiat2Application implements CommandLineRunner{

	public static void main(String[] args) throws IOException {
		String text = "Hello world";
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(new File("example.txt")));
			output.write(text);
		} catch ( IOException e ) { e.printStackTrace();
		} finally { if ( output != null ) output.close();}
		SpringApplication.run(Plagiat2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
