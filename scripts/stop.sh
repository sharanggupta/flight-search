#!/bin/bash

# Stop all services and clean up

set -e

echo "🛑 Stopping Flight Search services..."

docker-compose down

echo ""
echo "✅ All services stopped"
echo ""
echo "💡 To remove volumes (database data): docker-compose down -v"
