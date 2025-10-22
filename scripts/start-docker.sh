#!/bin/bash

# Start full application stack in Docker
# This script builds and runs both PostgreSQL and the app in Docker containers

set -e

echo "🚀 Starting Flight Search Application Stack (Docker)"
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Build and start all services
echo "📦 Building and starting all services..."
docker-compose up --build -d

echo ""
echo "⏳ Waiting for application to be ready..."
sleep 10

# Check if app is responding
until curl -s http://localhost:8080/api/flights?origin=JFK > /dev/null 2>&1; do
    echo "   Still waiting..."
    sleep 2
done

echo ""
echo "✅ Application is ready!"
echo ""
echo "📊 Service Status:"
docker-compose ps
echo ""
echo "🌐 API Available at: http://localhost:8080"
echo "   Example: curl http://localhost:8080/api/flights?origin=JFK"
echo ""
echo "📝 View logs: docker-compose logs -f"
echo "🛑 Stop services: docker-compose down"
