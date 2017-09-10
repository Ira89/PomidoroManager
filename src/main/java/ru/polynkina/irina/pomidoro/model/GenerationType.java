package ru.polynkina.irina.pomidoro.model;

public enum  GenerationType {

    ONCE, EVERY_DAY, EVERY_WEEK, EVERY_MONTH;

    public static GenerationType valueOfEnum(String text) throws Exception {
        return valueOf(text.trim());
    }

    public static GenerationType getValueByIndex(int index) {
        switch(index) {
            case 0: return ONCE;
            case 1: return EVERY_DAY;
            case 2: return EVERY_WEEK;
            default: return EVERY_MONTH;
        }
    }

    public static int getIndexByValue(GenerationType type) {
        switch(type) {
            case ONCE: return 0;
            case EVERY_DAY: return 1;
            case EVERY_WEEK: return 2;
            default: return 3;
        }
    }

    public static String[] getTextTypes() {
        return new String[] {"Один раз", "Кажый день", "Каждую неделю", "Каждый месяц"};
    }
}
