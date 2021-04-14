# ⚠️ We are looking for contributors - [discussion](https://github.com/BAData/protobuf-converter/issues/22).

[![Build Status](https://api.travis-ci.org/BAData/protobuf-converter.svg)](https://travis-ci.org/BAData/protobuf-converter)
[![GitHub release](https://img.shields.io/github/release/BAData/protobuf-converter.svg)](https://github.com/BAData/protobuf-converter/releases)
[![JitPack repository](https://jitpack.io/v/BAData/protobuf-converter.svg)](https://jitpack.io/#BAData/protobuf-converter)

# Protobuf converter

**protobuf-converter** is library for transforming your Domain Model Objects into Google Protobuf Messages and vice versa.

## Maven/Gradle

You can add this library as a dependency to your Maven or Gradle project through [JitPack](https://jitpack.io/#BAData/protobuf-converter).

## How to use it ?

Domain model classes that have to be transformed into protobuf messages must satisfy conditions:

-   Class has to be marked by _@ProtoClass_ annotation that contains reference on related protobuf message class.
-   Class fields has to be marked by _@ProtoField_ annotation. These fields must have getters and setters.

E.g.:

```java
@ProtoClass(ProtobufUser.class)
public class User {

	@ProtoField
	private String name;
	@ProtoField
	private String password;

	// getters and setters for 'name' and 'password' fields
	...
}
```

Code for conversion User instance into related protobuf message:

```java
User userDomain = new User();
...
ProtobufUser userProto = Converter.create().toProtobuf(ProtobufUser.class, userDomain);
```

Code for backward conversion:

```java
User userDomain = Converter.create().toDomain(User.class, userProto);
```

Conversion of lists of objects is similar to single object conversion.

### @ProtoClass

Annotation attibutes:

-   _value_ - required. Contains reference to protobuf message class.
-   _mapper_ - optional. Contains reference to class that performs field value mapping between domain and protobuf objects during conversion. Default value - **DefaultMapperImpl.class**

_mapper_ attribute is useful when domain model class and protobuf message class has different structure. In such case you have to create own **Mapper** implementation and specify it as value of _mapper_ attribute.

If type of domain class field is complex (marked by **@ProtoClass**) protobuf-converter will convert this field value to related protobuf message (**DefaultMapperImpl.class** has to be specified as mapper attribute value)

### @ProtoField

Annotation attributes:

1. _name_ - optional. Related protobuf field name.
2. _converter_ - optional. Contains reference to class that performs field value transformation. Default value - **DefaultConverterImpl.class**
3. _nullValue_ - optional. Contains reference to class that test protobuf object field for nullability . Default value - **DefaultNullValueInspectorImpl.class**
4. _defaultValue_ - optional. Contains reference to class that initialize domain object field if related protobuf field is not initialized. Default value - **SimpleDefaultValueImpl.class**

Use _name_ attribute when domain class field name is different from name specified in the \*.proto file.

_converter_ attribute is useful when domain class field type is different from protobuf message field type. E.g.: date value in the domain class is stored into **java.util.Date** and protobuf message uses int64(**java.lang.Long**). For field data transformation you must specify **DateLongConverterImpl.class** as value of _converter_ attribute. You can create own field type converter if you implement **TypeConverter** interface.

### Fields Ignore

In case when you need to prevent transforming some domain fields values into protobuf message field values use **FieldsIgnore.class**.

E.g.: field _password_ of **User.class** has not been transformed into **ProtobufUser.class** field when server generates response on user info request sent by client application. You need to add field _password_ into ignored list:

```java
User userDomain = new User();
...
FieldsIgnore ignoredFiedls = new FieldsIgnore().add(User.class, "password");
Configuration configuration = Configuration.builder().addIgnoredFields(ignoredFiedls).build();
ProtobufUser userProto = Converter.create(configuration).toProtobuf(ProtobufUser.class, userDomain);
```

Also **FieldsIgnore.class** allows to ignore single field as well as field data types. E.g.: it is possible to ignore all fields with type **java.lang.String**:

```java
User userDomain = new User();
...
FieldsIgnore ignoredFiedls = new FieldsIgnore().add(String.class);
Configuration configuration = Configuration.builder().addIgnoredFields(ignoredFiedls).build();
ProtobufUser userProto = Converter.create(configuration).toProtobuf(ProtobufUser.class, userDomain);
```

### Obfuscation

Main Proguard options:

```
-keep interface net.badata.protobuf.converter.** { *; }

-keep public class * implements net.badata.protobuf.converter.mapping.Mapper {
     public <init>();
}

-keep public class * implements net.badata.protobuf.converter.type.TypeConverter {
     public <init>();
}

-keep public class * implements net.badata.protobuf.converter.inspection.DefaultValue {
     public <init>();
}

-keep public class * implements net.badata.protobuf.converter.inspection.NullValueInspector {
     public <init>();
}
```

Keep your domain objects (replace **your.package.name** with name of package where domain objects stored):

```
-keepclassmembers @net.badata.protobuf.converter.annotation.ProtoClass public class your.package.name.** {
     @net.badata.protobuf.converter.annotation.ProtoField <fields>;
     public <init>();
     public void set*(***);
     public boolean is*();
     public  *** get*();
}
```

### Example

**protobuf-converter** capabilities is demonstrated by project located in the _example_ folder.

Assemble example project:

```
gradle assemble
```

Start server:

```
java -jar example.jar "server"
```

Start client:

```
java -jar example.jar
```

# License

MIT License
