# TestGradle
Various integrations using Gradle

**Example outputs:**

```java
JSON
-----------------
{"anInt":4,"strings":["A","B","C"],"astring":"A","abool":true}
Result length: 62

ION
-----------------
{anInt:4,strings:["A","B","C"],astring:"A",abool:true}
Result length: 54

YAML
-----------------
---
anInt: 4
strings:
- "A"
- "B"
- "C"
astring: "A"
abool: true

Result length: 65

XML
-----------------
<TestPojo><anInt>4</anInt><strings><strings>A</strings><strings>B</strings><strings>C</strings></strings><astring>A</astring><abool>true</abool></TestPojo>
Result length: 155

CSV
-----------------
4,A;B;C,true,A

Result length: 15

SCHEMA:

{
  "type" : "record",
  "name" : "TestPojo",
  "namespace" : "com.howell.matt.test",
  "fields" : [ {
    "name" : "anInt",
    "type" : {
      "type" : "int",
      "java-class" : "java.lang.Integer"
    }
  }, {
    "name" : "strings",
    "type" : [ "null", {
      "type" : "array",
      "items" : "string"
    } ]
  }, {
    "name" : "abool",
    "type" : "boolean"
  }, {
    "name" : "astring",
    "type" : [ "null", "string" ]
  } ]
}
AVRO
-----------------
0802060241024202430001020241
Result length: 28

CBOR
-----------------
bf65616e496e740467737472696e6773836141614261436761737472696e6761416561626f6f6cf5ff
Result length: 82

SCHEMA:

// com.howell.matt.test.TestPojo

// Message for com.howell.matt.test.TestPojo
message TestPojo {
  optional int32 anInt = 1;
  repeated string strings = 2;
  optional string astring = 3;
  optional bool abool = 4;
}

PROTOBUF
-----------------
08041201411201421201431a01412001
Result length: 32
```
