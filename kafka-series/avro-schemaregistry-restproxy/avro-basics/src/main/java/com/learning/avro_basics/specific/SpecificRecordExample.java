package com.learning.avro_basics.specific;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import com.learning.avro_basics.beans.Customer;

public class SpecificRecordExample {

	private static final String SERIALZED_FILE_NAME = "customer-specific.avro";

	public static void main(String[] args) {
		//let maven create pojo with fields of strict data types
		
		Customer customer = Customer.newBuilder().setFirstName("Vivek")
				.setLastName("Anand")
				.setAge(27)
				.setHeight(176)
				//no default value set and hence wont able to create object
				.setWeight(70.01f)
				//.setAutomatedEmail(false)
				.build();
		
		System.out.println("generate customer: "+customer);
		
		Schema schema = customer.getSchema();
		
		if (customer != null && schema != null) {
			// writing to a file
			// serializing step of the generic object
			// this can be shared over network and can be deserialzed by consumer on kafka
			// ecospace
			final DatumWriter<Customer> datumWriter = new SpecificDatumWriter<>(Customer.class);
			try (DataFileWriter<Customer> dataFileWriter = new DataFileWriter<>(datumWriter)) {
				dataFileWriter.create(schema, new File(SERIALZED_FILE_NAME));
				dataFileWriter.append(customer);
				System.out.println("Written customer-specific.avro");
				dataFileWriter.close();
			} catch (IOException e) {
				System.out.println("Couldn't write file");
				e.printStackTrace();
			}
		}

		if (customer != null && schema != null) {
			// reading from a file
			//deserialzation step
			final File file = new File(SERIALZED_FILE_NAME);
			final DatumReader<Customer> datumReader = new SpecificDatumReader<>(Customer.class);
			Customer customerRead;
			try (DataFileReader<Customer> dataFileReader = new DataFileReader<>(file, datumReader)) {
				customerRead = dataFileReader.next();
				System.out.println("Successfully read avro file: ");
				System.out.println(customerRead.toString());

				// get the data from the generic record
				System.out.println("First name: " + customerRead.getFirstName());

				// read a non existent field
				//does not work in specific record for generic record returns null
				//System.out.println("Non existent field: " + customerRead.get("not_here"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
