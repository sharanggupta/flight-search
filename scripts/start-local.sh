#!/bin/bash

# Start local development environment
# This script starts PostgreSQL in Docker and runs the app locally

set -e

echo "🚀 Starting Flight Search Local Development Environment"
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Start PostgreSQL container
echo "📦 Starting PostgreSQL container..."
docker-compose -f docker/docker-compose.yml up -d postgres

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
until docker-compose -f docker/docker-compose.yml exec -T postgres pg_isready -U postgres > /dev/null 2>&1; do
    sleep 1
done

echo "✅ PostgreSQL is ready!"
echo ""
echo "🗄️  Database initialized with sample flight data"
echo ""
echo "🏃 Starting Spring Boot application..."
echo "   Profile: local"
echo "   Port: 8080"
echo ""

# Run the application with local profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
