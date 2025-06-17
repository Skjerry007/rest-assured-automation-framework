#!/bin/bash

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to setup AWS Secrets Manager
setup_aws_secrets() {
    echo "Setting up AWS Secrets Manager..."
    
    if ! command_exists aws; then
        echo "Installing AWS CLI..."
        if [[ "$OSTYPE" == "darwin"* ]]; then
            brew install awscli
        else
            echo "Please install AWS CLI manually for your OS"
            exit 1
        fi
    fi
    
    # Configure AWS credentials
    aws configure
    
    # Create a test secret
    aws secretsmanager create-secret \
        --name "test-secret" \
        --description "Test secret for automation framework" \
        --secret-string '{"key":"value"}'
}

# Function to setup Google Secret Manager
setup_google_secrets() {
    echo "Setting up Google Secret Manager..."
    
    if ! command_exists gcloud; then
        echo "Installing Google Cloud SDK..."
        if [[ "$OSTYPE" == "darwin"* ]]; then
            brew install google-cloud-sdk
        else
            echo "Please install Google Cloud SDK manually for your OS"
            exit 1
        fi
    fi
    
    # Initialize gcloud
    gcloud init
    
    # Create a test secret
    gcloud secrets create test-secret \
        --replication-policy="automatic"
    
    echo '{"key":"value"}' | \
    gcloud secrets versions add test-secret --data-file=-
}

# Main script
echo "Setting up secret management..."

# Check which secret manager to use
read -p "Which secret manager would you like to use? (aws/google): " secret_manager

case $secret_manager in
    aws)
        setup_aws_secrets
        ;;
    google)
        setup_google_secrets
        ;;
    *)
        echo "Invalid choice. Please choose 'aws' or 'google'"
        exit 1
        ;;
esac

echo "Secret management setup complete!" 