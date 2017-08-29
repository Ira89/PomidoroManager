package ru.polynkina.irina.pomidoro;

public enum  GenerationType {

    ONCE, EVERY_DAY, EVERY_WEEK, EVERY_MONTH;

    public static GenerationType valueOfEnum(String text) throws Exception {
        return valueOf(text.trim());
    }

    public static String[] getTextTypes() {
        return new String[] {"Один раз", "Кажый день", "Каждую неделю", "Каждый месяц"};
    }
}
