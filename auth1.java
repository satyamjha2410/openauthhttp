package com.example.auth;

import com.opencsv.CSVWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.*;
import java.io.BufferedReader;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {

		String filePath = "/Users/satyamkumarjha/data222.csv";
		File file = new File(filePath);
		BufferedReader reader;
		try{
			reader = new BufferedReader(new FileReader("/Users/satyamkumarjha/myfile.txt"));
			String line = reader.readLine();
			int i=0;
			int correct_case_number = 0;

			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);
			String[] header = { "cat_id", "msq", "dd_vendor" };
			String[] cat_array={"574-7120", "684-4195","154-9337","100-0991","665-4122"};
			writer.writeNext(header);
			while (line != null)
			{
				// read next line
				line = reader.readLine();
				//String cat_id= line.substring(2, 10);
				//String msq= line.substring(21,line.length()-1);
				String msq="NO msq";
				String cat_id=cat_array[i];
				String Url1 = "https://api.tesco.com/product/v3/products?catid=";
				String Url2 = "&extendedFields=all&clientId=trn%3Atesco%3Acid%3Aed20e16f-e391-4e46-a3f0-f38af17fd9da%3Ad419a6a3-c27d-4915-9962-218bde9523fa";
				String Url  = Url1+cat_id+Url2;


				RestTemplate restTemplate = new RestTemplate();
				String token_value="b5075a24-1b0c-4f3e-a655-ea10895ea0bb";
				String client_id_value = "trn:tesco:cid:ed20e16f-e391-4e46-a3f0-f38af17fd9da:d419a6a3-c27d-4915-9962-218bde9523fa";
				//setting the headers
				HttpHeaders headers = new HttpHeaders();
				headers.add("Authorization", "Bearer " + token_value);
				headers.add("client_id", client_id_value);

				//headers.add("client_secret", client_secret_value);


				HttpEntity entity = new HttpEntity(headers);

				//executing the GET call
				HttpEntity<String> response = restTemplate.exchange(Url, HttpMethod.GET, entity, String.class);

				//retrieving the response
				String ans = response.getBody();
				//System.out.println("Response"+ ans);

				String match = "dd_vendor_part_number";
				int position = ans.indexOf(match);
				if(position == -1) {
					i++;
					System.out.println("dd_vendor_part_number Not present");
					String[] data2 = { cat_id,msq, "dd_vendor_not present" };
					writer.writeNext(data2);
					continue;

				}
				System.out.println(position);
				String sub1 = ans.substring(position+64);
				String dd_vendor = sub1.substring(0,sub1.indexOf('}')-1);
				//String sub1=ans.substring(position+64, position+75);
//				if(msq == dd_vendor)
//				{
//
//					correct_case_number++;
//				}
				String[] data1 = { cat_id,msq, dd_vendor };
				writer.writeNext(data1);
				System.out.println(cat_id +" : dd_vendor = "+ dd_vendor + " msq = " + msq );

				//line = reader.readLine();
				if(i >= 4)
				{
					break;
				}
				i++;
				System.out.println(correct_case_number);
			}
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}




	}
}
