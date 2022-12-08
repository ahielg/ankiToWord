package anki;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class AnkiExport {

    private Connection connect(String fileLocation) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + fileLocation;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    /**
     * select all rows in the warehouses table
     */
    public List<AnkiQuestion> queryQuestions(String fileLocation) {
       /* String sql = "select  json_extract(decks, '$.'||c.did||'.name') as topic, sfld as sub, flds as data from notes n join cards c on c.nid=n.id " +
                "join col co on  json_extract(decks, '$.'||c.did||'.id') = c.did";*/

        String sql = "select  json_extract(decks, '$.'||c.did||'.name') as topic, sfld as sub, substr(sfld, 0, instr(sfld, ',')) as sub1, substr(sfld, instr(sfld, ', ')+2) as sub2, flds as data from notes n join cards c on c.nid=n.id \n" +
                "join col co on  json_extract(decks, '$.'||c.did||'.id') = c.did";

        List<AnkiQuestion> ankiQuestions = new ArrayList<>();
        try (Connection conn = this.connect(fileLocation);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                String data = rs.getString("data");
                String[] qa = data.split("\u001F");
                AnkiQuestion question = AnkiQuestion.builder()
                        .question(parseQA(qa, 0))
                        .answer(parseQA(qa, 1))
                        .clue(parseQA(qa, 3))
                        .topic(rs.getString("topic"))
                        .subTopic(rs.getString("sub"))
                        .siman(rs.getString("sub1"))
                        .seif(rs.getString("sub2"))
                        .build();

                ankiQuestions.add(question);
                //  System.out.println(question);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ankiQuestions;
    }

    private String parseQA(String[] qa, int i) {
        if (qa == null) {
            return null;
        }
        if (qa.length > i) {
            return qa[i];
        }
        return null;
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public static void main(String[] args) throws IOException {
        AnkiExport anki = new AnkiExport();
        //String fileZip = "c://Users/User/Downloads/t2.colpkg";
        String fileZip = "c://Users/User/Downloads/t.apkg";
        File ankiDbFile = UnzipFile.unzip(fileZip);

        //String fileDB = "C://Users/User/AppData/Roaming/Anki2/user/collection.anki2";
        //File ankiDbFile = new File(fileDB);
        List<AnkiQuestion> ankiQuestions = anki.queryQuestions(ankiDbFile.getAbsolutePath());

        Map<String, List<AnkiQuestion>> questionsByTopic = ankiQuestions.stream()
                .collect(groupingBy(q -> q.getSubTopic().replaceAll("'", "").replaceAll("\"", "")));
        SortedSet<String> sortedTopics = new TreeSet<>(questionsByTopic.keySet());

        AtomicReference<String> currTopic = new AtomicReference<>("");
        AtomicInteger currQuestion = new AtomicInteger(1);
        sortedTopics.stream()
                .filter(t -> t.contains("סי רעא") ||  t.contains("סי רעט"))
                .map(questionsByTopic::get)
                .forEach(questions -> {
                    AnkiQuestion firstQuestion = questions.get(0);
                    String[] topic = firstQuestion.getTopic().split("::");
                    if (!currTopic.get().equals(topic[topic.length - 1])) {
                        currTopic.set(topic[topic.length - 1]);
                        System.out.println("*" + topic[topic.length - 2] + " - " + topic[topic.length - 1] + "*");
                        currQuestion.set(1);
                    }
                    System.out.println("*" + firstQuestion.getSubTopic() + "*");
                    questions//.stream()
                            //.map(AnkiQuestion::getQuestion)
                            //.map(AnkiExport::html2text)
                            .forEach(q -> {
                                System.out.println("❓ " + currQuestion.getAndIncrement() + ". " + html2text(q.getQuestion()));
                                String clue = q.getClue();
                                System.out.println(toBrString(clue));
                                System.out.println();
                            });
                });

        //ConvertInXHTMLFile.createDocx("<b>שלום</b> אחיאל!!!");
    }

    private static String toBrString(String clue) {
        return html2text(notNull(clue).replaceAll("<br>", "~")).replaceAll("~",System.lineSeparator()).replaceAll("\\:\\)", ")");
    }

    private static String notNull(String str) {
        return (str != null) ? str : "";
    }
}