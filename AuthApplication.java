package com.example.auth;

import com.opencsv.CSVWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import java.io.*;
import java.io.BufferedReader;
import java.util.Objects;


public class AuthApplication {

	public static void main(String[] args) {

		int matched_no=0;
		int not_matched_number = 0;
		int not_found_number = 0;
		String filePath = "/Users/satyamkumarjha/resultu1000.csv";
		File file = new File(filePath);
		BufferedReader reader;
		try{
			reader = new BufferedReader(new FileReader("/Users/satyamkumarjha/myfile.txt"));
			String line = reader.readLine();
			int i=0;
			int correct_case_number = 0;
			int test_case = 1000;
			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);
			String[] header = { "s.No","cat_id", "msq", "dd_vendor","match" };
			writer.writeNext(header);
			while (line != null)
			{
				// read next line
				line = reader.readLine();
				String cat_id= line.substring(2, 10);
				String msq= line.substring(21,line.length()-1);
				//String cat_id="574-7120";
				String Url1 = "https://api.tesco.com/product/v3/products?catid=";
				String Url2 = "&extendedFields=all&clientId=trn%3Atesco%3Acid%3Aed20e16f-e391-4e46-a3f0-f38af17fd9da%3Ad419a6a3-c27d-4915-9962-218bde9523fa";
				String Url  = Url1+cat_id+Url2;


				RestTemplate restTemplate = new RestTemplate();
				String token_value="afd8e8b1-5c13-410c-bd47-d60488aa2daa";
				String client_id_value = "trn:tesco:cid:ed20e16f-e391-4e46-a3f0-f38af17fd9da:d419a6a3-c27d-4915-9962-218bde9523fa";
				//setting the headers
				HttpHeaders headers = new HttpHeaders();
				headers.add("Authorization", "Bearer " + token_value);
				headers.add("client_id", client_id_value);
				HttpEntity entity = new HttpEntity(headers);

				//executing the GET call
				HttpEntity<String> response = restTemplate.exchange(Url, HttpMethod.GET, entity, String.class);

				//retrieving the response
				String ans = response.getBody();

				String match = "dd_vendor_part_number";
				int position = ans.indexOf(match);

				if(position == -1) {
					i++;
					not_found_number++;
					System.out.println("dd_vendor_part_number Not present");
					String serial = String.valueOf(i);
					String[] data2 = { serial,cat_id,msq, "dd_vendor_not present","-"};
					writer.writeNext(data2);
					if(i >= test_case)
					{
						break;
					}
					continue;

				}
				System.out.println(position);
				String sub1 = ans.substring(position+64);
				String dd_vendor = sub1.substring(0,sub1.indexOf('}')-1);

				String serial = String.valueOf(i);
				if(Objects.equals(msq,dd_vendor))
				{
					matched_no++;
					String[] data1 = {serial,cat_id,msq, dd_vendor,"YES" };
					writer.writeNext(data1);
				}
				else
				{
					String[] data11 = {serial,cat_id,msq, dd_vendor,"NO" };
					writer.writeNext(data11);
					not_matched_number++;
				}

				System.out.println(cat_id +" : dd_vendor = "+ dd_vendor + " msq = " + msq );

				if(i >= test_case)
				{
					break;
				}
				i++;
				System.out.println(correct_case_number);
			}

			System.out.println(matched_no+" "+not_found_number+"  "+not_matched_number);
			String[] results={"Res:","Match = "+String.valueOf(matched_no),"Not match = "+String.valueOf(not_matched_number),"Not found = "+String.valueOf(not_found_number)};
			writer.writeNext(results);
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
