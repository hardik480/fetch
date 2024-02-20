package com.receipt.processor.service.service;

import com.receipt.processor.service.entity.Item;
import com.receipt.processor.service.entity.Receipt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReceiptService {

    private final Map<String, Integer> receiptPointsMap = new HashMap<>();

    public String processReceipt(Receipt receipt) {
        String id = UUID.randomUUID().toString();
        int points = calculatePoints(receipt);
        receiptPointsMap.put(id, points);
        return id;
    }

    public int getPoints(String id) {
        return receiptPointsMap.getOrDefault(id, -1);
    }

    private int calculatePoints(Receipt receipt) {
        int points = 0;

        // Rule 1: One point for every alphanumeric character in the retailer name.
            points += receipt.getRetailer().replaceAll("[ $&+,:;=?@#|'<>.^*()%!-]","").length();


        // Rule 2: 50 points if the total is a round dollar amount with no cents.
        if (receipt.getTotal() == Math.round(receipt.getTotal()))
            points += 50;

        // Rule 3: 25 points if the total is a multiple of 0.25.
        if (receipt.getTotal() % 0.25 == 0)
            points += 25;

        // Rule 4: 5 points for every two items on the receipt.
        points += receipt.getItems().size() / 2 * 5;

        // Rule 5: If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer.
        for (Item item : receipt.getItems()) {
            if (item.getShortDescription().trim().length() % 3 == 0) {
                points += (int) Math.round(item.getPrice() * 0.2);
            }
        }

        // Rule 6: 6 points if the day in the purchase date is odd.
        int purchaseDay = receipt.getPurchaseDate().getDayOfMonth();
        if (purchaseDay % 2 != 0)
            points += 6;

        // Rule 7: 10 points if the time of purchase is after 2:00pm and before 4:00pm.
        int purchaseHour = receipt.getPurchaseTime().getHour();
        int purchaseMinutes = receipt.getPurchaseTime().getMinute();
        if (purchaseHour+(0.01*purchaseMinutes) > 14 && purchaseHour+(0.01*purchaseMinutes) < 16)
            points += 10;

        return points;
    }
}
