package com.learning.avro_basics.generic;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

public class GenericRecordExample {
	private static final String SERIALZED_FILE_NAME = "customer-generic.avro";

	public static void main(String[] args) {
		Schema schema = null;
		Record customerRecord = null;
		try {
			schema = new Schema.Parser().parse(new File(
					"C:/Kanishk/learning/spring-kafka/git_folders/spring-kafka/kafka-series/avro-schemaregistry-restproxy/avro-basics/src/main/resources/avro/customers-schema.avsc"));

			GenericRecordBuilder genericRecordBuilder = new GenericRecordBuilder(schema);

			genericRecordBuilder.set("firstName", "Kanishk");
			genericRecordBuilder.set("lastName", "Bhatt");
			genericRecordBuilder.set("age", 29);
			genericRecordBuilder.set("height", 177.11f);
			genericRecordBuilder.set("weight", 70.2f);
			// genericRecordBuilder.set("fake_property", 70.2f);

			customerRecord = genericRecordBuilder.build();

			System.out.println("customer record generated: " + customerRecord);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (customerRecord != null && schema != null) {
			// writing to a file
			// serializing step of the generic object
			// this can be shared over network and can be deserialzed by consumer on kafka
			// ecospace
			final DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
			try (DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter)) {
				dataFileWriter.create(customerRecord.getSchema(), new File(SERIALZED_FILE_NAME));
				dataFileWriter.append(customerRecord);
				System.out.println("Written customer-generic.avro");
				dataFileWriter.close();
			} catch (IOException e) {
				System.out.println("Couldn't write file");
				e.printStackTrace();
			}
		}

		if (customerRecord != null && schema != null) {
			// reading from a file
			// deserialzation step
			final File file = new File(SERIALZED_FILE_NAME);
			final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
			GenericRecord customerRead;
			try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader)) {
				customerRead = dataFileReader.next();
				System.out.println("Successfully read avro file: ");
				System.out.println(customerRead.toString());

				// get the data from the generic record
				System.out.println("First name: " + customerRead.get("firstName"));

				// read a non existent field
				System.out.println("Non existent field: " + customerRead.get("not_here"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
