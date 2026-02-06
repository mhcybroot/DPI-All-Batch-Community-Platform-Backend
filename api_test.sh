#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080/api"

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${GREEN}Starting API Tests...${NC}"

# 1. Register User (Member)
echo -e "\n${GREEN}[1] Registering New User (Member)...${NC}"
curl -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "member@example.com",
    "password": "password123",
    "fullName": "John Doe"
  }'

# 2. Login as Seeded Admin
echo -e "\n${GREEN}[2] Logging in as Seeded Admin (admin@example.com)...${NC}"
ADMIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "admin123"
  }')
echo "Response: $ADMIN_RESPONSE"
ADMIN_TOKEN=$(echo $ADMIN_RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

if [ -z "$ADMIN_TOKEN" ]; then
  echo -e "${RED}Failed to login as Admin. Token not found.${NC}"
  # Exit or continue? Let's verify flow.
else
  echo "Admin Token: $ADMIN_TOKEN"
  
  # 3. Create Moderator User (Admin Only)
  echo -e "\n${GREEN}[3] Creating Moderator User (via Admin API)...${NC}"
  CREATE_MOD_RESPONSE=$(curl -s -X POST "$BASE_URL/admin/users" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
      "email": "moderator@example.com",
      "password": "modpassword",
      "fullName": "Site Moderator",
      "roles": ["MODERATOR"]
    }')
  echo "Response: $CREATE_MOD_RESPONSE"

  # 4. Login as New Moderator
  echo -e "\n${GREEN}[4] Logging in as New Moderator...${NC}"
  MOD_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{
      "email": "moderator@example.com",
      "password": "modpassword"
    }')
  echo "Response: $MOD_RESPONSE"
fi

# 5. Login as Member
echo -e "\n${GREEN}[5] Logging in as Member...${NC}"
MEMBER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "member@example.com",
    "password": "password123"
  }')
MEMBER_TOKEN=$(echo $MEMBER_RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
echo "Member Token: $MEMBER_TOKEN"

# 6. Admin: Get Pending Approvals
echo -e "\n${GREEN}[6] Fetching Pending Approvals (Admin)...${NC}"
curl -X GET "$BASE_URL/admin/approvals" \
  -H "Authorization: Bearer $ADMIN_TOKEN"

# 7. Member: Get Profile
echo -e "\n${GREEN}[7] Get My Profile (Member)...${NC}"
curl -X GET "$BASE_URL/profile/me" \
  -H "Authorization: Bearer $MEMBER_TOKEN"

echo -e "\n${GREEN}Tests Completed.${NC}"
