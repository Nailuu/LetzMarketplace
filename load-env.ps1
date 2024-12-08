$envFile = ".\.env"

if (Test-Path $envFile) {
    # Read each line of the .env file
    Get-Content $envFile | ForEach-Object {
        # Skip empty lines and comments
        if ($_ -match "^[^#].*") {
            # Split by first '=' symbol to get key and value
            $key, $value = $_ -split '=', 2
            if ($key -and $value) {
                # Set the environment variable
                [System.Environment]::SetEnvironmentVariable($key, $value, [System.EnvironmentVariableTarget]::Process)
            }
        }
    }
    Write-Host "Environment variables loaded successfully."
} else {
    Write-Host "No .env file found."
}
