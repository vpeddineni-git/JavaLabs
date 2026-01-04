$json = Get-Content 'src\main\resources\static\ticketpvctest.json' -Raw
Write-Host "Calling /api/zendesk/ticket/repository with JSON payload..."
Write-Host "JSON Content:"
Write-Host $json
Write-Host ""
Write-Host "Sending POST request..."
try {
    $response = Invoke-RestMethod -Uri 'http://localhost:8081/api/zendesk/ticket/repository' -Method Post -ContentType 'application/json' -Body $json -TimeoutSec 10
    Write-Host "SUCCESS! Ticket Created:"
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "ERROR: $_"
}
