.PHONY: all assembly style test test_success test_failure
run = scala -cp target/scala-*/bin/*.jar example.sc

all: assembly style

assembly:
	sbt assembly

style:
	sbt scalastyle

test: test_success test_failure style
	@echo --- All tests passed.

test_success: assembly
	@echo --- Testing successful interpretation of well formatted file.
	$(run) test-scripts/success.sc

test_failure: assembly
	@echo --- Testing handling of erroneous program text.
	@echo --- An error message should be printed.
	! $(run) test-scripts/failure.sc

