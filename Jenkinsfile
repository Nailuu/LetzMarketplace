pipeline {
    agent {
        label 'docker-agent'
    }

    triggers {
        pollSCM('H/3 * * * *')
    }

    environment {
        CORS = credentials('CORS')
        JWT_SECRET_KEY = credentials('JWT_SECRET_KEY')
        DB_HOST = credentials('DB_HOST')
        DB_PORT = 5432
        DB_NAME = "lmkp"
        DB_USERNAME = credentials('DB_USERNAME')
        DB_PASSWORD = credentials('DB_PASSWORD')
        TEST_DB_HOST = credentials('TEST_DB_HOST')
        TEST_DB_PORT = 5432
        TEST_DB_NAME = "test"
        TEST_DB_USERNAME = credentials('TEST_DB_USERNAME')
        TEST_DB_PASSWORD = credentials('TEST_DB_PASSWORD')
    }

    stages {
        stage('Build') {
            steps {
                withMaven(traceability: true) {
                    sh 'mvn --version'
                    sh 'mvn -B -DskipTests clean package'
                }
            }
        }

        stage('Migrate Database') {
            when {
                expression {
                    def changes = sh(script: "git diff --name-only HEAD~1 HEAD", returnStdout: true).trim()
                    return changes.contains('src/main/resources/db/migration')
                }
            }
            agent {
                docker {
                    image 'postgres:16'
                    args '--network="host"'
                }
            }
            steps {
                echo "SQL schemas changes detected, running migration..."
                sh 'flyway -url=jdbc:postgresql://${TEST_DB_HOST}:${TEST_DB_PORT}/${TEST_DB_NAME} \
                           -user=${TEST_DB_USERNAME} \
                           -password=${TEST_DB_PASSWORD} \
                           -locations=filesystem:./src/main/resources/db/migration \
                           migrate'
            }
        }

        stage('Test') {
            agent {
                docker {
                    image 'postgres:16'
                     args '--network="host"'
                }
            }
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
}