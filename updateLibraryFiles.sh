#! /bin/bash

outDir="SharedResources/out"

diagramCoreJsPath="$outDir/diagram-core.js"
diagramCoreDefPath="$outDir/diagramCore.d.ts"

twoDModelCoreJsPath="$outDir/two-d-model-core.js"
twoDModelCoreDefPath="$outDir/two-d-model-core.d.ts"

cp "$diagramCoreJsPath" "examples/StandaloneExample/src/main/webapp/resources/js"

cp "$diagramCoreDefPath" "Robots_diagram/src/main/webapp/app"
cp "$diagramCoreDefPath" "StepicRobotsWeb/src/main/webapp/app"
cp "$diagramCoreDefPath" "examples/OverlayExample/src/main/webapp/resources/types/diagram-core" 
cp "$diagramCoreDefPath" "examples/StandaloneExample/src/main/webapp/resources/types/diagram-core"

cp "$twoDModelCoreDefPath" "Robots_diagram/src/main/webapp/app"
cp "$twoDModelCoreDefPath" "StepicRobotsWeb/src/main/webapp/app"
