package mikenikitin.plagiat2.services;

import mikenikitin.plagiat2.model.Wordbook;
import mikenikitin.plagiat2.repository.WordbookRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LoadDictionary {

    private static WordbookRepository wordbookRepository;

    public static void wordbookcheck(){
//      List<Wordbook> wblist = wordbookRepository.findAll();
//      wblist.sort((a,b)->(a.getWord().compareTo(b.getWord())));
//      for (Wordbook word:wblist) System.out.print(word.getWord()+" ");
        Map<String, Wordbook> map = new HashMap<>();
        for (Wordbook w:wordbookRepository.findAll()) map.put(w.getWord(),w);
//      map.forEach((key,word)-> System.out.print(word.getWord()+" "));
        System.out.println(map.size());
    }

    public static int readFileLineByLine(String fileName) {
        Map<String, Wordbook> map = new HashMap <>();
        for (Wordbook wb:wordbookRepository.findAll()) map.put(wb.getWord(),wb);
//        System.out.println(map.size());
        int ln=0;
        System.out.println("loading WORDBOOK "+fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();
            while (line != null) {
                System.out.print("*"); // System.out.println(line);
                String words[] = line.toLowerCase().split("#")[1].split(",");
                for (String str:words)
                {
                    Wordbook wb=map.get(str.replaceAll("[^а-яё]",""));
                    if (wb!=null)
                        if ((wb.getDescription()==null)||(wb.getDescription()==""))
                        {
                            System.out.print(str);
                            wb.setDescription(str);
                            wordbookRepository.save(wb);
                            System.out.print(" ");
                            ln++;
                        }
                }
                line = br.readLine();
            }
        } catch (IOException e) {e.printStackTrace();}
        System.out.println();
        System.out.println(ln);
        return ln;
    }

    private static int WordBook(String file){
        System.out.println("loading LOPATIN "+file);
        List<String> lines = Collections.emptyList();
        try { lines = Files.readAllLines(Paths.get(file), UTF_8);
        } catch (IOException e) {e.printStackTrace();}
        for (String line:lines) {
            String word[] = line.split("#");
            Wordbook wbr = wordbookRepository.findByWord(word[0]);
            if (wbr != null) {
                wbr.setDescription(word[1].split("%")[0]); // %
                wordbookRepository.save(wbr); System.out.print(word[0]);
            } else System.out.print('.');
        }
        return lines.size();
    }

}
