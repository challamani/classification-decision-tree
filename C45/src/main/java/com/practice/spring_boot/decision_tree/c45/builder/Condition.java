package com.practice.spring_boot.decision_tree.c45.builder;

import java.util.function.Predicate;

/**
 * In decision tree we need to build the condition node, it has to part of decision tree building.
 * */
public class Condition {

    private String type;
    private Predicate predicate;

    public Predicate getPredicate() {
        return predicate;
    }

    private Condition(ConditionBuilder conditionBuilder){
        type = conditionBuilder.type;
        createPredicate(type, conditionBuilder);
    }

    @Override
    public String toString() {
        return "Condition{" +
                "type='" + type + '\'' +
                ", predicate=" + predicate +
                '}';
    }

    public String getType() {
        return type;
    }

    private void createPredicate(String type, ConditionBuilder conditionBuilder){
        switch (type) {
            case ConditionBuilder.BETWEEN:
                Predicate<Double> predicate = (a) -> (conditionBuilder.firstValue <= a && conditionBuilder.secondValue >= a);
                this.predicate = predicate;
                break;
            case ConditionBuilder.STRING_EQ:
                Predicate<String> stringPredicate = (a) -> (conditionBuilder.stringValue.equalsIgnoreCase(a));
                this.predicate = stringPredicate;
                break;
            case ConditionBuilder.NUM_EQ:
                Predicate<Integer> numberPredicate = (a) -> (conditionBuilder.number == a);
                this.predicate = numberPredicate;
                break;
            case ConditionBuilder.LESS_THAN:
                Predicate<Double> lessThanPredicate = (a) -> (conditionBuilder.firstValue > a);
                this.predicate = lessThanPredicate;
                break;
            case ConditionBuilder.GREATER_THAN:
                Predicate<Double> greaterThanPredicate = (a) -> (conditionBuilder.firstValue < a);
                this.predicate = greaterThanPredicate;
                break;
        }


    }

    public static class ConditionBuilder{

        private static final String BETWEEN="BETWEEN";
        private static final String STRING_EQ="STRING_EQ";
        private static final String NUM_EQ="NUMBER_EQ";
        private static final String LESS_THAN="LESS_THAN";
        private static final String GREATER_THAN="GREATER_THAN";

        private String stringValue;
        private String type;
        private double firstValue;
        private double secondValue;
        private Integer number;



        public Condition build(){
            return new Condition(this);
        }

        private ConditionBuilder stringEquals(String str){
            type=STRING_EQ;
            stringValue = str;
            return this;
        }

        private ConditionBuilder numberEquals(Integer num){
            type=NUM_EQ;
            number = num;
            return this;
        }

        private ConditionBuilder greaterThan(Double value){
            type=GREATER_THAN;
            firstValue = value;
            return this;
        }

        private ConditionBuilder lessThan(Double value){
            type=LESS_THAN;
            firstValue = value;
            return this;
        }

        private ConditionBuilder betweenValue(Double a, Double b){
            type=BETWEEN;
            firstValue = a;
            secondValue = b;
            return this;
        }

        public ConditionBuilder setType(String type){
            this.type=type;
            return this;
        }

        public ConditionBuilder setValue(String value){
            switch (type) {
                case BETWEEN:
                    String[] values = value.split(",");
                    return betweenValue(Double.parseDouble(values[0]), Double.parseDouble(values[1]));
                case LESS_THAN:
                    return lessThan(Double.parseDouble(value));
                case GREATER_THAN:
                    return greaterThan(Double.parseDouble(value));
                case NUM_EQ:
                    return numberEquals(Integer.parseInt(value));
                case STRING_EQ:
                    return stringEquals(value);
                default:
                    type = STRING_EQ;
                    return stringEquals(value);

            }
        }


    }
}
