package anki;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnkiQuestion {
    String question;
    String answer;
    String clue;
    String topic;
    String subTopic;
    String siman;
    String seif;
}
