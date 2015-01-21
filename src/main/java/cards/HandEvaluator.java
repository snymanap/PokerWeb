package cards;

import cards.Card;
import cards.Hand;
import cards.Rank;
import cards.Suit;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HandEvaluator {

    public static boolean isStraightFlush(Hand hand) {
        /*Collections.sort(hand.getCards(), new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o1.getRank().compareTo(o2.getRank());
            }
        });

        Rank previousRank = null;
        Suit previousSuit = null;
        for (Card card : hand.getCards()) {
            if (previousRank != null && card.getRank().ordinal() != previousRank.ordinal() + 1) {
                return false;
            }

            if (previousSuit != null && card.getSuit() != previousSuit) {
                return false;
            }

            previousRank = card.getRank();
            previousSuit = card.getSuit();
        }

        return true;

        /*

         */
        List<Rank> ranks = hand.getCards().stream().map(c -> c.getRank()).collect(Collectors.toList());
        return hand.getCards().stream().allMatch(c -> c.getSuit() == hand.getCards().get(0).getSuit())
                && (ranks.stream().mapToInt(r-> r.ordinal()).max().getAsInt() -
                ranks.stream().mapToInt(r -> r.ordinal()).min().getAsInt() == 4)
                && ranks.stream().distinct().count() == 5;
    }

    //map function Map<String, Long> cardCount = new HashMap<>();
    //cardCount.put("A", 3L);
    //cardCount.put("4", 4L);
    //......
    //System.out

    public static boolean isFourOfAKind(Hand hand)
    {

        List<Long> counted = hand.getCards().stream()
                .collect(Collectors.groupingBy(o -> o.getRank().toString(), Collectors.counting()))
                .values().stream().sorted().collect(Collectors.toList());
        if (counted.size() == 2 && counted.get(1) == 4)
        {
            return true;
        }
        return false;
        /*
        ----------------------------------------------
        Map<Rank, Integer> c = new HashMap<>();
        for (Card card : hand.getCards())
        {
            c.put(card.getRank(), 0);
        }
        for (Card card : hand.getCards())
        {
            c.put(card.getRank(), c.get(card.getRank()) + 1);
        }
            return c.containsValue(4);

         */
        /*int counter = 1;
        for (int i = 0; i < hand.getCards().size(); i++)
        {
            for (int j = 0; j  <hand.getCards().size(); j++)
            {
                if ((i != j) && ((hand.getCards().get(i).getRank().toString()).compareTo(hand.getCards().get(j).getRank().toString()) == 0))
                {
                    counter++;
                }

            }
            if (counter == 4) return true;
            else counter = 1;
        }*/



        //return false;
    }

    public static boolean isFullHouse(Hand hand) {
        List<Long> counted = hand.getCards().stream()
                .collect(Collectors.groupingBy(o -> o.getRank().toString(), Collectors.counting()))
                .values().stream().sorted().collect(Collectors.toList());
        if (counted.size() == 2 && counted.get(0) == 2 && counted.get(1) == 3)
        {
            return true;
        }
        return false;
    }

    public static boolean isFlush(Hand hand)
    {
        /*if (isStraightFlush(hand))
            return false;

        Collections.sort(hand.getCards(), new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o1.getRank().compareTo(o2.getRank());
            }
        });

        Rank previousRank = null;
        Card prev = null;
        for (Card card : hand.getCards())
        {
            if (previousRank != null && card.getRank().ordinal() == previousRank.ordinal() + 1) {
                return false;
            }
            if (prev != null && prev.getSuit() != card.getSuit())
                return false;
            else prev = card;
        }*/

        if (isStraightFlush(hand)) return false;

        List<Rank> ranks = hand.getCards().stream().map(c -> c.getRank()).collect(Collectors.toList());
        return hand.getCards().stream().allMatch(c -> c.getSuit() == hand.getCards().get(0).getSuit())

                && ranks.stream().distinct().count() == 5;
    }

    public static boolean isStraight(Hand hand)
    {
        /*Collections.sort(hand.getCards(), new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o1.getRank().compareTo(o2.getRank());
            }
        });
        boolean flag = false;
        for (int i = 1; i < hand.getCards().size(); i++)
        {
            if (hand.getCards().get(i).getSuit() != hand.getCards().get(0).getSuit()) {
                flag = true;
                break;
            }
        }

        if (!flag) return false;

        Rank previousRank = null;
        //Suit previousSuit = null;
        for (Card card : hand.getCards()) {
            if (previousRank != null && card.getRank().ordinal() != previousRank.ordinal() + 1) {
                return false;
            }

            //if (previousSuit != null && card.getSuit() != previousSuit) {
              //  return false;
            //}

            previousRank = card.getRank();
            //previousSuit = card.getSuit();
        }
        return true;*/
        List<Rank> ranks = hand.getCards().stream().map(c -> c.getRank()).collect(Collectors.toList());
        return (ranks.stream().mapToInt(r-> r.ordinal()).max().getAsInt() -
                ranks.stream().mapToInt(r -> r.ordinal()).min().getAsInt() == 4)
                && ranks.stream().distinct().count() == 5;
    }

    public static boolean isThreeOfAKind(Hand hand)
    {
        /*int counter = 1;
        for (int i = 0; i < hand.getCards().size(); i++)
        {
            for (int j = 0; j  <hand.getCards().size(); j++)
            {
                if ((i != j) && ((hand.getCards().get(i).getRank().toString()).compareTo(hand.getCards().get(j).getRank().toString()) == 0))
                {
                    counter++;
                }
            }
            if (counter == 3) return true;
            else counter = 1;
        }

        return false;*/

        List<Long> counted = hand.getCards().stream()
                .collect(Collectors.groupingBy(o -> o.getRank().toString(), Collectors.counting()))
                .values().stream().sorted().collect(Collectors.toList());
        if (counted.size() == 3 && counted.get(2) == 3)
        {
            return true;
        }
        return false;
    }

    public static boolean isOnePair(Hand hand) {
        /*int counter = 1;
        for (int i = 0; i < hand.getCards().size(); i++)
        {
            for (int j = 0; j  <hand.getCards().size(); j++)
            {
                if ((i != j) && ((hand.getCards().get(i).getRank().toString()).compareTo(hand.getCards().get(j).getRank().toString()) == 0))
                {
                    counter++;
                }
            }
            if (counter == 2) return true;
            else counter = 1;
        }

        return false;*/
        List<Long> counted = hand.getCards().stream()
                .collect(Collectors.groupingBy(o -> o.getRank().toString(), Collectors.counting()))
                .values().stream().sorted().collect(Collectors.toList());
        if (counted.size() == 4 && counted.get(3) == 2)
        {
            return true;
        }
        return false;
    }

    public static boolean isTwoPair(Hand hand) {
        List<Long> counted = hand.getCards().stream()
                .collect(Collectors.groupingBy(o -> o.getRank().toString(), Collectors.counting()))
                .values().stream().sorted().collect(Collectors.toList());
        if (counted.size() == 3 && counted.get(2) == 2 && counted.get(1) == 2)
        {
            return true;
        }
        return false;
    }
}
