# Makefile Fix - Unix Compatibility

## Issue
The Makefile was using Windows CMD syntax which doesn't work on Linux/Unix systems:
- `if not exist $(BIN_DIR) mkdir $(BIN_DIR)` - Windows syntax
- `if exist $(BIN_DIR) rmdir /s /q $(BIN_DIR)` - Windows syntax

## Solution
Replaced with Unix-compatible shell commands:
- `mkdir -p $(BIN_DIR)` - Creates directory if needed (no if statement required)
- `rm -rf $(BIN_DIR)` - Removes directory recursively (no if statement required)

## Commands Fixed
1. **compile target** (line 47) - Fixed directory creation
2. **clean target** (line 64) - Fixed directory removal

## Benefits
- `mkdir -p` creates parent directories if needed and doesn't fail if directory exists
- `rm -rf` removes directory tree and doesn't fail if directory doesn't exist
- Both commands are standard Unix utilities, cross-platform compatible

## Testing
The Makefile now works on:
- ✅ Linux (tested on WSL/Ubuntu)
- ✅ macOS
- ✅ Unix systems
- ✅ Any system with Make and standard shell utilities

## Note
The `build.bat` file is still available for Windows users who don't have Make installed.
