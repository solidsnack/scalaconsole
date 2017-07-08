.PHONY: all assembly style test test_success test_import test_failure
run = java -jar target/scala-*/uber.jar

ci: test style

all: assembly style

assembly:
	sbt --error assembly

style:
	sbt --error scalastyle

test: test_success test_import test_failure
	@echo --- All tests passed.

test_success: assembly
	@echo --- Testing successful interpretation of well formatted file.
	$(run) test-scripts/success.sc

test_import: assembly
	@echo --- Testing imports from within JAR that is running interpreter.
	$(run) test-scripts/import.sc

test_failure: assembly
	@echo --- Testing handling of erroneous program text.
	@echo --- An error message should be printed.
	! $(run) test-scripts/failure.sc

