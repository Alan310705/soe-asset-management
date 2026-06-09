import type { FixedAsset } from '../types/asset.types';

const TERMINAL_STATUSES = new Set(['COMPLETED', 'REJECTED']);

type WorkflowRequest = { assetId: string; status: string };

function activeWorkflowAssetIds(requests: WorkflowRequest[]): Set<string> {
  return new Set(
    requests.filter((r) => !TERMINAL_STATUSES.has(r.status)).map((r) => r.assetId),
  );
}

/** Assets eligible for a new handover or liquidation request. */
export function filterAssetsAvailableForWorkflow(
  assets: FixedAsset[],
  handovers: WorkflowRequest[],
  liquidations: WorkflowRequest[],
): FixedAsset[] {
  const blocked = new Set([
    ...activeWorkflowAssetIds(handovers),
    ...activeWorkflowAssetIds(liquidations),
  ]);
  return assets.filter((a) => a.status !== 'LIQUIDATED' && !blocked.has(a.id));
}
