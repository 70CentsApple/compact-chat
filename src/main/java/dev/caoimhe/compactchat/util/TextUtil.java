package dev.caoimhe.compactchat.util;

import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;

import java.util.function.Predicate;

public class TextUtil {
    /**
     * A regex pattern to match a timestamp of HH:mm(:ss?).
     * It can be surrounded by any character.
     * Also washes the color codes (if there is) for once
     */
    private static final String TIMESTAMP_PATTERN = "(§.)*.?\\d{1,2}:\\d{2}(:\\d{2})*.?\\s*(§.)*";

    /**
     * Most mods add create a new text literal and append the original text to it.
     * This method removes the timestamp from the literal text content.
     */
    public static Text removeTimestamps(Text text) {
        var content = text.getContent();
        if (!(content instanceof PlainTextContent textContent)) {
            return text;
        }

        var string = text.getString().trim();
        var withoutTimestamps = string.replaceAll(TIMESTAMP_PATTERN, "").trim();
        if (withoutTimestamps.equals(string)) {
            return text;
        }

        var newText = Text.literal(withoutTimestamps.trim());
        newText.setStyle(newText.getStyle());

        return newText;
    }
}
