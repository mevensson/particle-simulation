package com.okayboom.particlesim.idealgaslaw;

import com.okayboom.particlesim.SimResult;

public interface SimToGasLawConverter {

	GasLawValue convert(SimResult result);

}
