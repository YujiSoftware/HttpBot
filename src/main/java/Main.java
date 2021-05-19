import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String[]> list = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Path.of("list.txt"))) {
            lines.forEach(url -> {
                try {
                    String[] record = new String[]{url, null, null, null};
                    list.add(record);

                    Document doc = Jsoup.connect("http://" + url).get();
                    record[1] = doc.select("title").text();
                    record[2] = doc.select("meta[name=description]").attr("content");
                    record[3] = doc.select("meta[name=keywords]").attr("content");

                    System.out.println(Arrays.toString(record));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("result.csv"))) {
            for (String[] row : list) {
                writer.write(String.join("\t", row));
                writer.newLine();
            }
        }
    }
}
