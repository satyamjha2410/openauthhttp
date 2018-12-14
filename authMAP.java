package com.example.auth;

import com.opencsv.CSVWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AuthApplication {

    public static void main(String[] args) {

        int matched_no = 0;
        int not_matched_number = 0;
        int not_found_number = 0;
        int msq_not_found = 0;
        String filePath = "/Users/satyamkumarjha/colins_msq.csv";
        File file = new File(filePath);
        BufferedReader reader;
        BufferedReader reader2;
        try {
            reader = new BufferedReader(new FileReader("/Users/satyamkumarjha/activeproducts.txt"));
            reader2 = new BufferedReader(new FileReader("/Users/satyamkumarjha/colins.txt"));
            String line = reader.readLine();
            String line2 =reader2.readLine();


            int i = 1;
            int index = 0;
            int correct_case_number = 0;
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] header = {"s.No", "cat_id", "msq"};
            Map<String,String> map = new HashMap<>();
            writer.writeNext(header);

            while (line != null) {
                // read next line
                index++;
                String cat_id = line.substring(0, 8);
                String msq = line.substring(9, line.length());
                map.put(cat_id,msq);
                System.out.println("index= " + index +" "+ cat_id + " " + msq);
                line = reader.readLine();
            }

            index = 0;
            for (Map.Entry<String,String> entry : map.entrySet())
            {
                index++;
                System.out.println(index+" "+"Key = " + entry.getKey() +
                        ", Value = " + entry.getValue());


            }
            index = 0;
            while(line2 != null)
            {

                String cat_id = line2.substring(0, 8);
                if(map.containsKey(cat_id))
                {
                    index++;
                    System.out.println(index + " " + cat_id);
                    String[] results = {String.valueOf(index),cat_id, map.get(cat_id)};
                    writer.writeNext(results);
                }

                line2 = reader2.readLine();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println(matched_no + " " + not_found_number + "  " + not_matched_number);
        }
    }
}
