.PHONY: all assembly style test test_success test_import test_failure
run = java -jar tmp/uber.jar

ci: test style

all: assembly style

assembly:
	sbt --error assembly
	@rm -f tmp/uber.jar
	cp target/scala-*/uber.jar tmp/uber.jar

style:
	sbt --error scalastyle

test: test_success test_import test_main test_failure
	@echo --- All tests passed.

test_success: assembly
	@echo --- Testing successful interpretation of well formatted file.
	$(run) test-scripts/success.sc

test_import: assembly
	@echo --- Testing imports from within JAR that is running interpreter.
	$(run) test-scripts/import.sc

test_main: assembly
	@echo --- Testing a "Main object" style script.
	@rm -f tmp/main-token
	$(run) test-scripts/main.sc tmp/main-token
	@test -f tmp/main-token || { echo 'Main object did not run!' ; exit 1 ;}

test_failure: assembly
	@echo --- Testing handling of erroneous program text.
	@echo --- An error message should be printed.
	! $(run) test-scripts/failure.sc

