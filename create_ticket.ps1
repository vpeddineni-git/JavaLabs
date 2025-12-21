# Wait for server to be ready
Write-Host "Waiting for server to be ready..."
Start-Sleep -Seconds 5

$uri = "http://localhost:8081/api/zendesk/ticket"
$payload = @{
    subject = "SAMPLE: Venkat - Ticket created via API"
    comment = @{
        body = "Hello, let's see how you or your agents can easily respond to and solve tickets."
    }
    priority = "normal"
    status = "open"
    requester = @{
        name = "Test User"
        email = "test.user@example.com"
    }
    assignee_id = 24385359002652
    group_id = 24385295701532
    tags = @("zendesk_accelerated_setup")
    custom_fields = @(
        @{
            id = 24385313090332
            value = $null
        }
    )
}

$json = $payload | ConvertTo-Json -Depth 10
Write-Host "Sending request to: $uri"
Write-Host "Payload:`n$json`n"

try {
    $response = Invoke-WebRequest -Uri $uri -Method POST -Body $json -ContentType "application/json" -ErrorAction Stop
    Write-Host "✓ SUCCESS!"
    Write-Host "Status Code: $($response.StatusCode)"
    Write-Host "Response:`n$($response.Content)`n"
} catch {
    Write-Host "✗ ERROR: $($_.Exception.Message)"
    Write-Host "Response: $($_.Exception.Response.StatusCode)"
}
