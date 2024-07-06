package dev.caoimhe.compactchat.core;

import dev.caoimhe.compactchat.config.Configuration;
import dev.caoimhe.compactchat.util.TextUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

/**
 * A chat message received by the client.
 * The Text isn't stored here for memory reasons, that's stored in the HashMap linking the message to this.
 */
public class ChatMessage {
    /**
     * A regex pattern to match " (X+)", X being any number, and '+' being optional.
     * It also ensures that the closing parentheses is the end of the string.
     */
    private static final String OCCURRENCES_TEXT_PATTERN_STRING = "\\([0-9]+[+]?\\)$";
    private static final Pattern OCCURRENCES_TEXT_PATTERN = Pattern.compile(OCCURRENCES_TEXT_PATTERN_STRING);

    /**
     * The styling for the occurrences text
     */
    private static final Style OCCURENCES_TEXT_STYLE = Style.EMPTY.withColor(Formatting.GRAY);

    /**
     * The amount of times this message has occurred.
     */
    public int occurrences = 1;

    /**
     * Increments the occurrences counter.
     */
    public void addOccurrence() {
        // To prevent lag occurring after spamming messages, let's stop modifying it after maximum occurrences.
        occurrences = Math.min(occurrences + 1, Configuration.getInstance().maximumOccurrences);
    }

    /**
     * Returns modifiedText appended with the amount of occurences.
     */
    public Text modifiedText(Text unmodifiedText) {
        if (occurrences == 1) {
            return unmodifiedText;
        }

        var occurrencesString = " (" + occurrences;
        if (occurrences >= 100) {
            occurrencesString += "+";
        }
        occurrencesString += ")";

        var occurrencesText = Text
            .literal(occurrencesString)
            .setStyle(OCCURENCES_TEXT_STYLE);

        return unmodifiedText.copy().append(occurrencesText);
    }

    public Text removeTextModifications(Text modifiedText) {
        return this.removeOccurencesText(TextUtil.removeTimestamps(modifiedText));
    }

    /**
     * Returns the text with occurrences stripped.
     */
    private Text removeOccurencesText(Text modifiedText) {
        return Text.literal(modifiedText.getString().replaceAll(OCCURRENCES_TEXT_PATTERN_STRING, ""));
    }
}
