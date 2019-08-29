# Test-time Contract Driven Development framework

In formal verification of algorithm we state relationships about and between its inputs, outputs, internal and external states.
 The set of those relationships are called contracts.

In TDD, we actually define those contracts with test cases.
Test cases can be thought as the code verifying the contract of a service, by testing it with a representative parameter for all cases of the logic of the service.

To conduct those tests, we need stubs for underlying services; services used by the service under test.
The stubs are also providing information using the representative parameter sets.
Those stubs are code providing the needed information about the contract of the underlying service.

That means that the contract of most of those services are coded more times: once as test cases verifying the service, and one or more times as stubs providing information about the service as an underlying one.

Coding the whole information once have the following advantages:
* less coding effort: the information is coded only once, instead of two or more times
* more structural approach: while there are approaches to make sure that test coverage is adequate, stubs are written in a more or less ad-hoc manner.
 Using the contract for both goals means that the process can use the structured approach of testing.
* integration assurance: as the same code is used for stubs and tests, we know that the information used about an underlying service is actually tested, so the service actually works as the stubs say it should work.

It can be argued that this approach is not actually contract-driven: we are not coding the contract with the mathematical precision needed in algorithm verification, we are still relying on test cases.
In other words we are not defining the contract formally and fully, just pinning it along representative data points.
So maybe the name is not correct. If you have a better name, just tell us.

Existing tools for contract driven development do not concentrate on testing; they either concentrate on 
* formal verification; this makes testing unnecessary, but needs very involved work
* run-time verification; as a way to augment formal verification with a less resource-intensive way for less critical parts of the software, and protect the critical parts against forbidden inputs.

This library is a work in progress. For an example of how a contract looks like see src/test/java/com/kodekonveyor/cdd/RunnerDataCreationServiceContract.java

