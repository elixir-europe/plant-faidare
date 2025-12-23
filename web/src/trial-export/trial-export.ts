import { Job } from '../trial/trial';

export function initializeTrialExport(options: { contextPath: string; jobId: string }) {
  const running = !document.querySelector<HTMLDivElement>('#running')!.classList.contains('d-none');
  if (running) {
    const interval = setInterval(() => refresh(options, interval), 5000);
  }
}

async function refresh(options: { contextPath: string; jobId: string }, interval: any) {
  const response = await fetch(`${options.contextPath}/observation-units/exports/${options.jobId}`);
  const job = (await response.json()) as Job;
  if (job.status === 'DONE') {
    document.querySelector<HTMLDivElement>('#running')!.classList.add('d-none');
    document.querySelector<HTMLDivElement>('#done')!.classList.remove('d-none');
    clearInterval(interval);
  } else if (job.status === 'FAILED') {
    document.querySelector<HTMLDivElement>('#running')!.classList.add('d-none');
    document.querySelector<HTMLDivElement>('#failed')!.classList.remove('d-none');
    clearInterval(interval);
  }
}
