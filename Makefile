.PHONY: all assembly style test test_success test_failure

all: assembly style

assembly:
	sbt assembly

style:
	sbt scalastyle

test: test_success test_failure style
	@echo --- All tests passed.

test_success: assembly
	@echo --- Testing successful interpretation of well formatted file.
	java -jar target/scala-*/bin/*.jar scala.success

test_failure: assembly
	@echo --- Testing handling of erroneous program text.
	@echo --- An error message should be printed.
	! java -jar target/scala-*/bin/*.jar scala.failure

