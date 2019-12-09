OPENAPI_BUILD_DIRECTORY := openapi/build
SPRING_OPENAPI_JAR := kotlin/build/libs/spring-openapi-0.0.1-SNAPSHOT.jar
KOTLIN_FILES := $(shell find kotlin/ -type f -name '*.kt')

all: \
	$(OPENAPI_BUILD_DIRECTORY)/greetings/.dirstamp \
	$(SPRING_OPENAPI_JAR)

$(OPENAPI_BUILD_DIRECTORY)/greetings/.dirstamp: openapi/greetings.yaml
	mkdir -p $(OPENAPI_BUILD_DIRECTORY)/greetings
	env JAVA_OPTS='-Dlog.level=warn' openapi-generator generate --generator-name=kotlin --input-spec=openapi/greetings.yaml --output=$(OPENAPI_BUILD_DIRECTORY)/greetings --package-name=com.github.brymck.greetings --group-id=com.github.brymck --artifact-id=greetings-sdk --artifact-version=0.0.1-SNAPSHOT --additional-properties=enumPropertyNaming=UPPERCASE,serializationLibrary=gson,collectionType=list
	patch --strip=0 < patches/build.gradle.patch
	mkdir -p kotlin/src/main/kotlin/com/github/brymck/greetings/models
	cp $(OPENAPI_BUILD_DIRECTORY)/greetings/src/main/kotlin/com/github/brymck/greetings/models/* kotlin/src/main/kotlin/com/github/brymck/greetings/models
	touch $@

$(SPRING_OPENAPI_JAR): $(KOTLIN_FILES) kotlin/build.gradle.kts kotlin/settings.gradle.kts
	cd kotlin && gradle build

openapi-serve: $(SPRING_OPENAPI_JAR)
	java -jar kotlin/build/libs/spring-openapi-0.0.1-SNAPSHOT.jar

openapi-test:
	python python/openapi_client_test.py

clean:
	rm -rf $(OPENAPI_BUILD_DIRECTORY)
	cd kotlin && gradle clean

.PHONY: \
	all \
	openapi-serve \
	openapi-test \
	clean
