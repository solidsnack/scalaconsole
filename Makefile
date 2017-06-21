.PHONY: all assembly test test_success test_failure

all: assembly

assembly:
	sbt assembly

test: test_success test_failure
	@echo --- All tests passed.

test_success: assembly
	@echo --- Testing successful interpretation of well formatted file.
	java -jar target/scala-*/bin/*.jar scala.success

test_failure: assembly
	@echo --- Testing handling of erroneous program text.
	@echo --- An error message should be printed.
	! java -jar target/scala-*/bin/*.jar scala.failure

