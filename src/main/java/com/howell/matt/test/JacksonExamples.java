package com.howell.matt.test;

import java.util.function.Supplier;

import org.apache.avro.Schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.ion.IonObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory;
import com.fasterxml.jackson.dataformat.protobuf.schema.NativeProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.collect.ImmutableList;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@ToString
@EqualsAndHashCode
public class JacksonExamples {

    private final TestPojo testPojo;

    public static void main(String[] args) throws Exception {
        final TestPojo testPojo = TestPojo.builder().aString("A").aBool(true).anInt(4).strings(ImmutableList.of("A",
            "B", "C")).build();

        final JacksonExamples examples = new JacksonExamples(testPojo);
        examples.run();
    }

    public void run() throws Exception {
        print("JSON", ObjectMapper::new, testPojo);
        print("ION", IonObjectMapper::new, testPojo);
        print("YAML", YAMLMapper::new, testPojo);
        print("XML", XmlMapper::new, testPojo);
        printWithWriter("CSV", this::csvWriter, testPojo);
        printAvro("AVRO", testPojo);
        printCBOR("CBOR", testPojo);
        printProtoBuf("PROTOBUF", testPojo);
        printSmile("SMILE", testPojo);
    }

    private void print(String type, Supplier<ObjectMapper> supplier, TestPojo object) throws Exception {
        print(type, supplier.get().writeValueAsString(object));
    }

    private void printWithWriter(String type, Supplier<ObjectWriter> supplier, TestPojo object) throws Exception {
        print(type, supplier.get().writeValueAsString(object));
    }

    private void print(String type, String result) throws Exception {
        System.out.println(type);
        System.out.println("-----------------");
        System.out.printf(result);
        System.out.println();
        System.out.printf("Result length: " + result.length());
        System.out.println();
        System.out.println();
    }

    private ObjectWriter csvWriter() {
        CsvMapper csvMapper = new CsvMapper();

        return csvMapper.writerFor(TestPojo.class).with(csvMapper.schemaFor(TestPojo.class));
    }

    private void printCBOR(final String type, final TestPojo testPojo) throws Exception{
        CBORFactory cborFactory = new CBORFactory();
        ObjectMapper mapper = new ObjectMapper(cborFactory);
        byte[] cborData = mapper.writeValueAsBytes(testPojo);

        print(type, bytesToHex(cborData));
    }

    private void printAvro(final String type, final TestPojo testPojo) throws Exception{
        ObjectMapper mapper = new ObjectMapper(new AvroFactory());
        AvroSchemaGenerator gen = new AvroSchemaGenerator();
        mapper.acceptJsonFormatVisitor(TestPojo.class, gen);
        AvroSchema schemaWrapper = gen.getGeneratedSchema();

        Schema avroSchema = schemaWrapper.getAvroSchema();
        String asJson = avroSchema.toString(true);
        System.out.println("SCHEMA:\n\n" + asJson);

        byte[] avroData = mapper.writerFor(TestPojo.class).with(schemaWrapper)
            .writeValueAsBytes(testPojo);

        print(type, bytesToHex(avroData));

    }

    private void printProtoBuf(final String type, final TestPojo testPojo) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new ProtobufFactory());
        ProtobufSchemaGenerator gen = new ProtobufSchemaGenerator();
        mapper.acceptJsonFormatVisitor(TestPojo.class, gen);
        ProtobufSchema schemaWrapper = gen.getGeneratedSchema();

        NativeProtobufSchema nativeProtobufSchema = schemaWrapper.getSource();
        String asProtofile = nativeProtobufSchema.toString();
        System.out.println("SCHEMA:\n\n" + asProtofile);

        byte[] protoBufData = mapper.writerFor(TestPojo.class).with(schemaWrapper)
            .writeValueAsBytes(testPojo);

        print(type, bytesToHex(protoBufData));
    }

    private void printSmile(final String type, final TestPojo testPojo) throws  Exception{
        ObjectMapper mapper = new ObjectMapper(new SmileFactory());
        byte[] smileData = mapper.writeValueAsBytes(testPojo);

        print(type, bytesToHex(smileData));

    }
    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
