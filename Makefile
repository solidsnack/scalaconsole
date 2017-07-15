.PHONY: all assembly style ci
.PHONY: test test_success test_import test_failure test_throw

run = java -jar tmp/uber.jar

ci: test style

all: assembly style

assembly:
	sbt --error assembly
	@rm -f tmp/uber.jar
	cp target/scala-*/uber.jar tmp/uber.jar

style:
	sbt --error scalastyle

test: test_success test_import test_main test_failure test_throw
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
	! $(run) test-scripts/failure.sc
	@echo --- Success: script failed.
	@echo --- An error message should have been printed.

test_throw: assembly
	@echo --- Testing that exceptions are rethrown.
	@rm -f tmp/first-line-of-stacktrace
	$(run) test-scripts/throw.sc 2>&1 | head -n1 >tmp/first-line-of-stacktrace
	fgrep -q 'A wild EXCEPTION appears...' tmp/first-line-of-stacktrace
	@echo --- Success: first line of stacktrace was the script exception.

