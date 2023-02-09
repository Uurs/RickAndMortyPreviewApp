package ua.bvar.rickmortyapp.core

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ua.bvar.rickmortyapp.di.MockDatabaseModule

class MockDatabaseRule: TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                MockDatabaseModule.cleanDB()
                base.evaluate()
                MockDatabaseModule.cleanDB()
            }
        }
    }
}