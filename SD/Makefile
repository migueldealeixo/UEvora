SRC_DIR = /home/miguel/uni/4ano/sd/sd-t01-51653-52215
BUILD_DIR = build
JAVAC = javac
JAVA = java
CLASSPATH = resources/postgresql.jar

all: compile

compile:
	$(JAVAC) -cp $(CLASSPATH) -d $(BUILD_DIR) $(SRC_DIR)/*.java

clean:
	rm -f $(BUILD_DIR)/*.class