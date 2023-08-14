pipeline {
    agent any
    environment {
        SDK = "C:\\Users\\davis\\AppData\\Local\\Android\\Sdk"
        PROJ_PATH = "C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\livestream"
    }

    parameters{
        string(
            defaultValue: "",
            name: "version_code"
        )
        string(
            defaultValue: "",
            name: "version_name"
        )
        text(
            defaultValue: "",
            name: "release_notes"
        )
        stashedFile 'google-service.json'
    }

    stages {
        stage('clean cache') {
            steps {
                bat 'fastlane gradleClean'
            }
        }

        stage('Configure app') {
            steps {
                unstash 'google-service.json'
                bat 'move google-service.json'
                bat 'gem -v'
                bat 'bundle install'
            }
        }

        stage('build') {
            steps {
                bat 'fastlane gradleBuild'
            }
        }

        stage('deploy') {
            steps {
                bat 'fastlane deploy VER_CODE: ${version_code} VER_NAME: ${version_name} RELEASE_NOTE: ${release_notes}'
            }
        }
    }
}