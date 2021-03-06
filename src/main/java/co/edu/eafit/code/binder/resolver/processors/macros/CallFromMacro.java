package co.edu.eafit.code.binder.resolver.processors.macros;

import co.edu.eafit.code.binder.resolver.json.macros.MacroJson;
import co.edu.eafit.code.binder.resolver.json.macros.MacroOutputType;
import co.edu.eafit.code.binder.resolver.processors.data.Macro;
import co.edu.eafit.code.generator.metamodel.arduino.classes.sketch.Sketch;
import co.edu.eafit.code.generator.metamodel.arduino.classes.sketch.SketchContext;

public class CallFromMacro extends Macro {

    @Override
    public void process(MacroJson json, Sketch sketch) {

        String[] parameters = json.getParameters();
        String[] fparams = null;

        String label = parameters[0];
        String function = parameters[1];

        if (parameters.length > 2) {

            fparams = new String[parameters.length - 2];

            for (int i = 2; i < parameters.length; i++)
                fparams[i - 2] = parameters[i];

        }

        SketchContext place = null;

        if (json.getMacroOutputType() == MacroOutputType.SETUP)
            place = sketch.getSetupFunction();

        if (place == null)
            throw new RuntimeException("La salida del macro '" + json.getMacroType().name() + "' es nula o no fue encontrada!");

        String[] finalFparams = fparams;

        place.addInstruction(codeBuffer -> {

            codeBuffer.append(label + "." + function + "(", true);
            if (finalFparams != null && finalFparams.length > 0)
                for (int i = 0; i < finalFparams.length; i++) {
                    String param = finalFparams[i];
                    codeBuffer.append((i + 1 == finalFparams.length) ? param : param + ", ", false);
                }

            codeBuffer.append(");", false);
            codeBuffer.appendBreakline();
            return false;

        });

    }

    @Override
    public String getValue() {
        return null;
    }

}
